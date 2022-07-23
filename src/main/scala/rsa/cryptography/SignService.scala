package rsa.cryptography

import rsa.key.PrivateKey

case class SignService(privateKey: PrivateKey) {
  private val decryptionService = DecryptionService(privateKey)

  def sign(message: BigInt): BigInt =
    decryptionService.decrypt(message)
}
