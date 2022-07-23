import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import rsa.conversion.{DecodingService, EncodingService}
import rsa.cryptography.{DecryptionService, EncryptionService, SignService, VerifyService}
import rsa.key.{KeyPair, KeyPairService, PrivateKey, PublicKey}
import rsa.prime.{PrimeService, Totient}

import java.security.SecureRandom

class RsaTest extends AnyFeatureSpec with GivenWhenThen {

  Feature("rsa") {
    Scenario("sign then verify") {
      Given("cipher text")
      val cipherText = 1070777

      And("prepare base")
      val p = 1223
      val q = 1987
      val n = p * q
      val totient = Totient.calcuate(p, q)

      And("prepare public key")
      val e = BigInt(948047)
      val publicKey = PublicKey(n, e)

      Then("verify that e is correct")
      publicKey.e.gcd(totient) shouldBe 1

      And("prepare private key")
      val d = publicKey.e.modInverse(totient)

      And("construct key pair")
      val keyPair = KeyPair(privateKey = PrivateKey(n, d), publicKey = PublicKey(n, e))

      And("sign service / verify service")
      val signService = SignService(keyPair.privateKey)
      val verifyService = VerifyService(keyPair.publicKey)

      And("signed cipher text")
      val signature = signService.sign(cipherText)

      When("verify signature")
      val verification = verifyService.verify(cipherText, signature)

      Then("positive verification of signature")
      verification shouldBe true
    }
    Scenario("encrypt then decrypt") {
      Given("message")
      val message = "hello"

      And("generated key pair")
      val KeyPair(privateKey, publicKey) = KeyPairService(new PrimeService(new SecureRandom)).generate()

      And("encryption / decryption services")
      val encryptionService = EncryptionService(publicKey)
      val decryptionService = DecryptionService(privateKey)

      When("cipher text with base64")
      val ciphertext = EncodingService.base64Encode(message)

      And("encrypted cipher")
      val encrypted = encryptionService.encrypt(ciphertext)

      When("decrypted message")
      val decrypted = decryptionService.decrypt(encrypted)

      And("decipher decrypted message")
      val deciphered = DecodingService.base64decode(decrypted)

      Then("deciphered decrypted message should be equal to input message")
      deciphered shouldBe message
    }
  }

}
