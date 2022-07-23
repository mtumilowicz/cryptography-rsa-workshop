package rsa

import rsa.cryptography.{SignService, VerifyService}
import rsa.key.{KeyPair, PrivateKey, PublicKey}

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

}
