package rsa

import java.security.SecureRandom

object Attacks extends App {

  def productAttack(): Unit = {
    val KeyPair(privateKey, publicKey) = KeyPairService(new PrimeService(new SecureRandom)).generate()

    val encryptionService = EncryptionService(publicKey)
    val decryptionService = DecryptionService(privateKey)

    val _387 = 387
    val _387Signed = decryptionService.decrypt(_387)
    val _387Verified = encryptionService.encrypt(_387Signed)
    println(_387)
    println(_387Verified)

    val _2 = 2
    val _2Signed = decryptionService.decrypt(_2)
    val _2Verified = encryptionService.encrypt(_2Signed)
    println(_2)
    println(_2Verified)

    val _774 = _2Signed * _387Signed
    val _774Verified = encryptionService.encrypt(_774)
    println(774)
    println(_774Verified)
  }

  productAttack()

  def smallE(): Unit = {
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
    val messageInt = EncodingService.encode(message, symbols)
    val cypher = encryptionService.encrypt(messageInt)

    val decoded = DecodingService.decode(Math.pow(cypher.toDouble, 1.0 / publicKey.e.toDouble).toInt, symbols)
    println(decoded)
  }

  smallE()

}
