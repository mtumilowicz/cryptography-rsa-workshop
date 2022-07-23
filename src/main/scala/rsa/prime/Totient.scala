package rsa.prime

object Totient {

  def calcuate(prime1: BigInt, prime2: BigInt): BigInt =
    (prime1 - 1) * (prime2 - 1)

}
