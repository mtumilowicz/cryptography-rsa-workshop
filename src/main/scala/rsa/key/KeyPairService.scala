package rsa.key

import rsa.prime.{PrimeService, Totient}

case class KeyPairService(primeService: PrimeService) {

  def generate(): KeyPair = {
    val p = primeService.generate()
    val q = primeService.generate()
    val n = p * q
    val totient = Totient.calcuate(p, q)

    val e = primeService.findFirstRelativePrime(totient)

    val d = e.modInverse(totient)

    KeyPair(PrivateKey(n, d), PublicKey(n, e))
  }

}
