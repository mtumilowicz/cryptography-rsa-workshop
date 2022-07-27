# cryptography-rsa-workshop

* references
    * https://stackoverflow.com/a/72461285
    * https://stackoverflow.com/questions/33105434/converting-a-base10-number-to-a-basen-number-using-a-custom-alphabet-of-size-n
    * https://gist.github.com/jasperdenkers/59cf5ad4acbba6b9d75d
    * https://crypto.stackexchange.com/questions/81495/rsa-is-there-a-way-to-digitally-sign-a-message-without-knowing-the-private-key
    * https://www.johndcook.com/blog/2019/03/06/rsa-exponent-3/
    * https://stackoverflow.com/questions/1967578/how-bad-is-3-as-an-rsa-public-exponent
    * https://crypto.stackexchange.com/questions/3608/why-is-padding-used-for-rsa-encryption-given-that-it-is-not-a-block-cipher
    * https://crypto.stackexchange.com/questions/22531/how-does-rsa-padding-work-exactly
    * https://www.encryptionconsulting.com/education-center/what-is-rsa/
    * https://www.comparitech.com/blog/information-security/rsa-encryption/
    * [Faster Primality Test - Applied Cryptography](https://www.youtube.com/watch?v=p5S0C8oKpsM)
    * https://en.wikipedia.org/wiki/Carmichael_number
    * [How To Tell If A Number Is Prime: The Miller-Rabin Primality Test](https://www.youtube.com/watch?v=zmhUlVck3J0)
    * https://blog.trailofbits.com/2019/07/08/fuck-rsa/
    * https://en.wikipedia.org/wiki/Carmichael_number
    * https://medium.com/@prudywsh/how-to-generate-big-prime-numbers-miller-rabin-49e6e6af32fb
    * [Cipher Block Chaining](https://www.youtube.com/watch?v=L4HaxfCRRs0)
    * [Encrypting with Block Ciphers](https://www.youtube.com/watch?v=oVCCXZfpu-w)

## preface
* goals of this workshop
* all used/needed math basis is here: https://github.com/mtumilowicz/cryptography-math-basics

## disclaimer
* only for workshop purposes
    * for example: given implementation of RSA does not have padding to reveal major flaw of RSA

## asymmetric cryptography
* symmetric cyptography
    * if Alice and Bob want to exchange messages, they must first mutually agree on a secret key k
    * but what if every communication between them is monitored by their adversary?
        * is it possible to exchange a secret key under these conditions?
            * first reaction: is that it is not possible => every piece of information
            that Alice and Bob exchange is public
        * solution: public key (or asymmetric) cryptography
* analogy
    * Alice: buys a safe with a narrow slot in the top and puts her safe in a public location
    * Bob: writes his message slips it through the slot
    * Alice: only a person with the key to the safe can retrieve Bob’s message
    * summary
        * public key: the safe
        * encryption algorithm: process of putting the message in the slot
        * decryption algorithm: process of opening the safe with the key
* mathematical formulation
    * spaces of
        * keys K
            * element k e K is a pair of keys: k = (k priv, k pub)
                * private key and the public key
        * plaintexts M
        * ciphertexts C
    * for each public key there is a corresponding encryption function e_kpub: M -> C
    * for each private key is a corresponding decryption function d_kpriv: C -> M
    * (k priv, k pub) e K => (d_kpriv) o (e_kpub) is identity on M
    * observation
        * for asymmetric cipher to be secure, it must be difficult to compute the decryption
        function, even knowing public key
            * under this assumption, Alice can send k pub to Bob and Bob can send back the
            ciphertext without worrying that anyone will be able to decrypt the message
        * to easily decrypt: it is necessary to know the private key
            * presumably only Alice has it
        * private key is sometimes called trapdoor information
            * it provides a trapdoor (i.e., a shortcut) for computing the inverse function of e_kpub
* vs symmetric cryptography
    * asymmetric ciphers tend to be considerably slower than symmetric ciphers
        * for that reason, first use an asymmetric cipher to send the key to a symmetric cipher
        * then use it to transmit the actual file
* encryption is a permutation of b-bit strings
    * {0, 1}^b -> {0, 1}^b
    * each key "chooses" some permutation

## trapdoor function
* is a function that is easy to compute in one direction, yet believed to be difficult to compute in the opposite
direction (finding its inverse) without special information, called the "trapdoor
* analogy
    * padlock and its key
    * it is trivial to change the padlock from open to closed without using the key
        * by pushing the shackle into the lock mechanism
    * opening the padlock easily requires the key to be used
        * key is the trapdoor
* example
    * y = x^e mod n
    * bad trapdoor function
        * n = p (prime)
        * y = x^e mod p
        * we have to find inverse of e mod (p - 1)
            * Fermat's little theorem => we can perform calculations mod (p − 1) in the exponent
            * ed = 1 mod (p - 1)
                * it is solvable (for example using extended Euclidean algorithm) if gdc(e, p-1) = 1
        * y^d = (x^e)^d = x^ed = x mod p
        * solution is unique
            * suppose that we have two solution c1, c2
            * c1 ≡ c1^de ≡ (c1^e)^d ≡ y^d ≡ (c2^e)^d ≡ c2^de ≡ c2 mod p
        * very easy to reverse
    * good trapdoor function (rsa)
        * n = pq, p,q - prime
        * y = x^e mod n
        * we have to find inverse of e mod φ(pq)
            * Euler's theorem => we can perform calculations mod φ(pq) in the exponent
            * ed = 1 mod φ(pq)
                * it is solvable (for example using extended Euclidean algorithm) if gdc(e, φ(pq)) = 1
            * d - decryption exponent
            * e - encryption exponent
        * solution is unique
            * c1 ≡ c1^de ≡ (c1^e)^d ≡ y^d ≡ (c2^e)^d ≡ c2^de ≡ c2 mod n
        * solving y = x^e mod n is as hard as factoring n = pq
        * however, if we know the actual factors, we can use Euler’s theorem and write x as
            * x = y^d mod n
            * ed = 1 mod φ(n)

## rsa
* asymmetric encryption algorithm
* named after its (public) inventors, Ron Rivest, Adi Shamir, and Leonard Adleman
* encryption is faster if e is a small and decryption is faster if d is small
    * most common e is 65537
* we are encrypting / decrypting numbers - not letters
    * how we encrypt a message like “hello”?
        * all of the information that our computers process is stored in binary (1s and 0s)
            * we use encoding standards like ASCII or Unicode to represent them in ways that humans can
            understand (letters)
        * this means that messages like “hello” already exist as numbers
            * can easily be computed in the RSA algorithm
* so the setup of RSA is choosing two large prime numbers
    * RSA relies on the size of its key to be difficult to break
        * longer an RSA key, the more secure it is
    * prime generating problem
        * how to test a given number n for being prime?
            * maybe use Fermat’s little theorem?
                * take A: gcd(A,n) == 1
                    * A^(n−1) ≠ 1 mod n => then n is composite otherwise, it is prime with some probability
                    * repeat for many As to increase the likelihood of being prime
                * why it's wrong?
                    * composite number that fulfills Fermat's theorem for any A: 561
                    * family of such numbers are called Carmichael numbers
        * Miller Rabin primarity test
            * p be an odd prime
            * p−1 = 2^k q, gcd(a,p)=1 => one of the following two conditions is true
                * a^q is congruent to 1 modulo p
                    * q = p-1 / 2^k
                * one of a^q, a^2q , a^4q ,..., a^2^(k−1)q is congruent to −1 modulo p
            * proof
                * n = 2^k * q + 1
                * a^(n-1) = 1 mod n
                * a^(n-1) - 1 = 0 mod n
                * (a^(n-1 / 2) - 1)(a^(n-1 / 2) + 1) = 0 mod n
                * (a^(n-1 / 4) - 1)(a^(n-1 / 4) + 1)(a^(n-1 / 2) + 1) = 0 mod n
                * (a^(n-1 / 2^k) - 1)(a^(n-1 / 2^k) + 1)*...*(a^(n-1 / 2) + 1) = 0 mod n
                    * we can expand it until n-1 / 2^k is odd
                * if n divides at least one multiplier => probably prime
                    * Euclid's lemma: if p prime <=> p|ab => p|a or p|b
                    * so we check this one by one
                    * each number in the list is the square of the previous number
                        * n-1 / 2^k, n-1 / 2^(k-1), n-1 / 2^(k-2)
            * if n is composite then running k iterations of the Miller–Rabin test will declare n probably
            prime with a probability at most 4^(−k)
                * proof
                    * Theorem 12.8
                    * https://math.mit.edu/classes/18.783/2017/LectureNotes12.pdf
            * prime number density
                * π(n) is the number of prime numbers ≤ n
                * prime number theorem states that n / ln(n) is a good approximation of π(n)
                * it means the probability that a randomly chosen number is prime is 1 / ln(n)
                    * there are n positive integers ≤ n
                    * approximately n / ln(n) primes
                    * n / ln(n) / n = 1 / ln(n)
                * probability to find a prime number of 1024 bits: (ln(2¹⁰²⁴)) = (1 / 710)
                    * primes are odd (except 2), we can increase this probability by 2
                    * to generate a 1024 bits prime number, we have to test 355 numbers randomly generated

## Padding
* structure of a message can give attackers clues about its content
* padding: adding randomized data to hide the original formatting
    * using the word padding for RSA is by now rather incorrect
        * historical reasons
    * old padding schemes for RSA did simply extend the message before converting a number
    * newer schemes actually alter the message itself as well
        * example: OAEP
            * entire message is randomly transformed before RSA modular exponentiation
* padding oracles
    * adding padding to a message requires the recipient to perform an additional check
    whether the message is properly padded
    * when the check fails, the server throws an invalid padding error
        * that single piece of information is enough to slowly decrypt a chosen message
        * process is tedious and involves manipulating the target ciphertext millions of times
            * isolating the changes to get valid padding
        * that one error message is all you need to eventually decrypt a chosen ciphertext
            * it makes developing secure libraries almost impossible
* RSA without padding is also called Textbook RSA
* RSA Encryption padding is randomized, ensuring that the same message encrypted multiple
times looks different each time

## block ciphers
* if block ciphers act on short blocks, how do we encrypt a long message?
* electronic codebook mode (ECB)
    * encrypt each block separately
    * example
        * codebook
            * 00 -> 11
            * 01 -> 00
            * 10 -> 01
            * 11 -> 10
        * plaintext: 00|11|00|01|00
        * cipher:    11|10|11|00|11
    * cons
        * patterns
* summary



## vulnerabilities
* TLS 1.3 no longer supports RSA
* criticism: https://www.youtube.com/watch?v=lElHzac8DDI
* Using prime factorization, researchers managed to crack a 768 bit key RSA algorithm, but it
took them 2 years, thousands of man hours, and an absurd amount of computing power, so the
currently used key lengths in RSA are still safe
* The National Institute of Science and Technology (NIST) recommends a minimum key length
of 2048 bits now, but many organizations have been using keys of length 4096 bits
* For example, p and q must be globally unique. If p or q ever gets reused in another
RSA moduli, then both can be easily factored using the GCD algorithm
* Briefly, as you've certainly observed the RSA primitive is based on modular exponentiation
and this operation is homomorphic in the sense that if someone knows two ciphertexts
c1=md1 and c2=md2, then he can immediately deduce : (m1.m2)d=c1.c2=c. This is the essential
reason to break this "homomorphism", by adding inside the plaintext some hash or other techniques.
* Sometimes the exponent is exponent 3, which is subject to an attack we’ll describe below [1].
(The most common exponent is 65537.)
        * This is an attack on “textbook” RSA because the weakness in this post could be
        avoiding by real-world precautions such as adding random padding to each message so
        that no two recipients are sent the exact same message.
        * By the way, a similar trick works even if you only have access to one encrypted
        message. Suppose you’re using a 2048-bit modulus N and exchanging a 256-bit key.
        If you message m is simply the key without padding, then m³ is less than N, and so
        you can simply take the cube root of the encrypted message in the integers.
          * If you use random padding such as OAEP in PKCS#1, most (all?) of the known
          weaknesses from using low exponents are no longer relevant.
        * Key length has a much higher practical impact on security. A 768-bit RSA key has been
        cracked recently (this was not easy ! Four years of work with big computers and bigger
        brains). A 1024-bit key is deemed adequate for the short term, but long-term uses (e.g.
        the encrypted data has high value and must still be confidential in year 2030) would
        mandate something bigger, e.g. 2048 bits.

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


