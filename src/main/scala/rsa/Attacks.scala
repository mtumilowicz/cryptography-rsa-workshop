package rsa

import rsa.conversion.{DecodingService, EncodingService}
import rsa.cryptography.{DecryptionService, EncryptionService, SignService, VerifyService}
import rsa.key.{KeyPair, KeyPairService}
import rsa.prime.PrimeService

import java.security.SecureRandom

object Attacks extends App {

  def productAttack(): Unit = {
    val KeyPair(privateKey, publicKey) = KeyPairService(new PrimeService(new SecureRandom)).generate()

    val verifyService = VerifyService(publicKey)
    val signService = SignService(privateKey)

    val message_387 = 387
    val _387Signed = signService.sign(message_387)
    val _387Verified = verifyService.verify(message_387, _387Signed)
    println(message_387)
    println(_387Verified)

    val message_2 = 2
    val _2Signed = signService.sign(message_2)
    val _2Verified = verifyService.verify(message_2, _2Signed)
    println(message_2)
    println(_2Verified)

    val message_774 = 774
    val _774Signed = _387Signed * _2Signed
    val _774Verified = verifyService.verify(message_774, _774Signed)
    println(774)
    println(_774Verified)
  }

  productAttack()

  def smallE(): Unit = {
    val KeyPair(privateKey, publicKey) = key.KeyPairService(new PrimeService(new SecureRandom)).generate()

    val encryptionService = cryptography.EncryptionService(publicKey)
    val decryptionService = cryptography.DecryptionService(privateKey)

    lazy val symbols = Array[Char](
      ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z'
    )

    val message = "hello"
    val messageInt = EncodingService.encode(message, symbols)
    val cypher = encryptionService.encrypt(messageInt)

    val decoded = DecodingService.decode(Math.pow(cypher.toDouble, 1.0 / publicKey.e.toDouble).toInt, symbols)
    println(decoded)
  }

  smallE()

}
