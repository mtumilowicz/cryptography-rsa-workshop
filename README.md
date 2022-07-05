
  /** Constructs a randomly generated positive BigInt that is probably prime,
   *  with the specified bitLength.
   */
  def apply(bitlength: Int, certainty: Int, rnd: scala.util.Random): BigInt =
    apply(new BigInteger(bitlength, certainty, rnd.self))

* https://stackoverflow.com/a/72461285
    * You just have to interpret a message as an integer written in another base system with different symbols
* https://stackoverflow.com/questions/33105434/converting-a-base10-number-to-a-basen-number-using-a-custom-alphabet-of-size-n
* https://gist.github.com/jasperdenkers/59cf5ad4acbba6b9d75d
* https://crypto.stackexchange.com/questions/81495/rsa-is-there-a-way-to-digitally-sign-a-message-without-knowing-the-private-key
* Euler's totient function