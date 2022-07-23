package rsa.cryptography

import rsa.key.PublicKey

case class VerifyService(publicKey: PublicKey) {
  private val encryptionService = EncryptionService(publicKey)

  def verify(message: BigInt, signature: BigInt): Boolean =
    encryptionService.encrypt(signature) == message
}
