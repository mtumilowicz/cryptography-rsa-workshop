package rsa.cryptography

import rsa.key.PublicKey

case class VerifyService(publicKey: PublicKey) {
  private val encryptionService = EncryptionService(publicKey)

  def verify(message: BigInt): BigInt =
    encryptionService.encrypt(message)
}
