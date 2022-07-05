package rsa

case class KeyPairService(primeService: PrimeService) {

  def generate(): KeyPair = {
    val p = primeService.generate()
    val q = primeService.generate()
    val n = p * q
    val totient = calcuateTotient(p, q)

    val e = primeService.findFirstRelativePrime(totient)

    val d = e.modInverse(totient)

    KeyPair(PrivateKey(n, d), PublicKey(n, e))
  }

  def calcuateTotient(prime1: BigInt, prime2: BigInt): BigInt =
    (prime1 - 1) * (prime2 - 1)

}
