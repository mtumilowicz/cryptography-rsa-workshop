package rsa

case class EncryptionService(publicKey: PublicKey) {
  def encrypt(message: BigInt): BigInt =
    message.modPow(publicKey.e, publicKey.n)
}
