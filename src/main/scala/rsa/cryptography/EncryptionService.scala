package rsa.cryptography

import rsa.key.PublicKey

case class EncryptionService(publicKey: PublicKey) {
  def encrypt(message: BigInt): BigInt =
    message.modPow(publicKey.e, publicKey.n)
}
