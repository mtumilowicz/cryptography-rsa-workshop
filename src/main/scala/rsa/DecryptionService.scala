package rsa

case class DecryptionService(privateKey: PrivateKey) {
  def decrypt(message: BigInt): BigInt =
    message.modPow(privateKey.d, privateKey.n)
}
