package rsa.prime

import java.security.SecureRandom
import scala.annotation.tailrec

class PrimeService(random: SecureRandom) {
  def generate(): BigInt =
    BigInt(64, 100, random)

  def findFirstRelativePrime(i: BigInt): BigInt = {

    @tailrec
    def loop(current: BigInt): BigInt =
      if (i.gcd(current) == 1) current
      else loop(current + 2)

    val initial = 3
    loop(initial)
  }
}
