# cryptography-rsa-workshop

* references
    * https://stackoverflow.com/a/72461285
    * https://stackoverflow.com/questions/33105434/converting-a-base10-number-to-a-basen-number-using-a-custom-alphabet-of-size-n
    * https://gist.github.com/jasperdenkers/59cf5ad4acbba6b9d75d
    * https://crypto.stackexchange.com/questions/81495/rsa-is-there-a-way-to-digitally-sign-a-message-without-knowing-the-private-key

## rsa
* RSA is named after its (public) inventors, Ron Rivest, Adi Shamir,
  and Leonard Adleman
* setup: Let p and q be large primes, let N = pq, and let e and c be integers
*  Problem. Solve the congruence x e ≡ c (mod N) for the variable x
* Easy. Bob, who knows the values of p and q, can easily solve for x as
  described in Proposition 3.5.
*  Hard. Eve, who does not know the values of p and q, cannot easily find x.
* Dichotomy. Solving x e ≡ c (mod N) is easy for a person who possesses
  certain extra information, but it is apparently hard for all other people.

## digital signature
* Encryption schemes, whether symmetric or asymmetric, solve the problem
  of secure communications over an insecure network
* Digital signatures solve
  a different problem, analogous to the purpose of a pen-and-ink signature on
  a physical document.
* Samantha 1 has a (digital) document D, for example a computer file, and she
  wants to create some additional piece of information D Sam that can be used
  to prove conclusively that Samantha herself approves of the document
  * So
    you might view Samantha’s digital signature D Sam as analogous to her actual
    signature on an ordinary paper document.
* analogy
    * To contrast the purpose and functionality of public key (asymmetric) cryp-
      tosystems versus digital signatures, we consider an analogy using bank deposit
      vaults and signet rings
    *  A bank deposit vault has a narrow slot (the “public
      encryption key”) into which anyone can deposit an envelope, but only the
      owner of the combination (the “private decryption key”) to the vault’s lock is
      able to open the vault and read the message
    * Thus a public key cryptosystem
      is a digital version of a bank deposit vault
    * A signet ring (the “private signing
      key”) is a ring that has a recessed image.
    * The owner drips some wax from
      a candle onto his document and presses the ring into the wax to make an
      impression (the “public signature”)
    * Anyone who looks at the document can
      verify that the wax impression was made by the owner of the signet ring, but
      only the owner of the ring is able to create valid impression
        * In today’s world, with its
          plentiful machine tools, signet rings and wax images obviously would not provide much
          security
* Digital signatures are at least as important as public key cryp-
  tosystems for the conduct of business in a digital age, and indeed, one might
  argue that they are of greater importance
  * To take a significant instance, your
    computer undoubtedly receives program and system upgrades over the Inter-
    net
  * How can your computer tell that an upgrade comes from a legitimate
    source, in this case the company that wrote the program in the first place?
  * The answer is a digital signature
  *  The original program comes equipped with
    the company’s public verification key
  * The company uses its private signing
    key to sign the upgrade and sends your computer both the new program and
    the signature
  * Your computer can use the public key to verify the signature,
    thereby verifying that the program comes from a trusted source, before in-
    stalling it on your system.
*  The natural capability of most digital signature schemes is to
  sign only a small amount of data, say b bits, where b is between 80 and 1000
   * It is thus quite inefficient to sign a large digital document D, both because it
     takes a lot of time to sign each b bits of D and because the resulting digital
     signature is likely to be as large as the original document.
   * The standard solution to this problem is to use a hash function, which is
     an easily computable function
        * Hash : (arbitrary size documents) −→ {0,1} k
   * Then, rather than signing her document D, Samantha instead
     computes and signs the hash Hash(D). For verification, Victor computes and
     verifies the signature on Hash(D).
     * More generally, one wants it to be very difficult
       to find two distinct inputs D and D ? whose outputs Hash(D) and Hash(D ? )
       are the same
* The setup is the same
  as for RSA encryption, Samantha chooses two large secret primes p and q
  and she publishes their product N = pq and a public verification exponent e.
  * Samantha uses her knowledge of the factorization of N to solve the congruence
    de ≡ 1 mod(p − 1)(q − 1)
  * Note that if Samantha were doing RSA encryption, then e would be her
    encryption exponent and d would be her decryption exponent
  * However, in
    the present setup d is her signing exponent and e is her verification exponent.
  * Signing: Sign document D by computing S ≡ D^d (mod N)
  * Verification: Compute S^e mod N and verify that it is equal to D
    * This process works because Euler’s formula: S^e ≡ D^de ≡ D (mod N)