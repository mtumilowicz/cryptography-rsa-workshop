package rsa.cryptography

import rsa.key.PrivateKey

case class DecryptionService(privateKey: PrivateKey) {
  def decrypt(message: BigInt): BigInt =
    message.modPow(privateKey.d, privateKey.n)
}
