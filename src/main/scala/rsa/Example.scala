package rsa

import rsa.conversion.{DecodingService, EncodingService}
import rsa.cryptography.{DecryptionService, EncryptionService, SignService, VerifyService}
import rsa.key.{KeyPair, KeyPairService, PrivateKey, PublicKey}
import rsa.prime.PrimeService

import java.security.SecureRandom

object Example extends App {

  def signingVerifying(): Unit = {
    val n = 1223 * 1987
    val e = 948047
    val d = 1051235
    val document = 1070777
    val keyPair = KeyPair(privateKey = PrivateKey(n, d), publicKey = PublicKey(n, e))
    val signService = SignService(keyPair.privateKey)
    val verifyService = VerifyService(keyPair.publicKey)

    val signature = signService.sign(document)
    val verification = verifyService.verify(document, signature)

    println(verification)
  }

  signingVerifying()

  def encryptDecrypt(): Unit = {
    val KeyPair(privateKey, publicKey) = KeyPairService(new PrimeService(new SecureRandom)).generate()
    val encryptionService = EncryptionService(publicKey)
    val decryptionService = DecryptionService(privateKey)

    lazy val symbols = Array[Char](
      ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z'
    )

    val message = "hello"
    val ciphertext = EncodingService.encode(message, symbols)

    val encrypted = encryptionService.encrypt(ciphertext)
    val decrypted = decryptionService.decrypt(encrypted)

    println(encrypted)
    println(DecodingService.decode(decrypted, symbols))
  }

  encryptDecrypt()

}
