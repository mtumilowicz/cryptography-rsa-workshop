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

## assymetric cryptography
* optimisations
    * To make things more efficient, a file will generally be encrypted with a symmetric-key algorithm, and then the symmetric key will be encrypted with RSA encryption. Under this process, only an entity that has access to the RSA private key will be able to decrypt the symmetric key.

      Without being able to access the symmetric key, the original file can’t be decrypted. This method can be used to keep messages and files secure, without taking too long or consuming too many computational resources.

* Trap door functions
    * RSA encryption works under the premise that the algorithm is easy to compute in one direction, but almost impossible in reverse.
    * As an example, if you were told that 701,111 is a product of two prime numbers, would you be able to figure out what those two numbers are?
        * Even with a calculator or a computer, most of us wouldn’t have any idea of where to start, let alone be able to figure out the answer.

* prime generating
    * Miller Rabin primarity test
        * p be an odd prime, p−1 = 2^k q, gcd(a,p)=1 => one of the following two conditions is true
            * a^q is congruent to 1 modulo p
            * one of a^q, a^2q , a^4q ,..., a^2^(k−1)q is congruent to −1 modulo p
        * proof
            * n should be odd, therefore n = 2^k * m + 1
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
## rsa
* The Rivest-Shamir-Adleman (RSA) encryption algorithm is an asymmetric encryption algorithm that is widely used
in many products and services

* vulnerabilities
    * RSA relies on the size of its key to be difficult to break. The longer an RSA key, the more secure it is.
    * Using prime factorization, researchers managed to crack a 768 bit key RSA algorithm, but it took them 2 years, thousands of man hours, and an absurd amount of computing power, so the currently used key lengths in RSA are still safe
    * The National Institute of Science and Technology (NIST) recommends a minimum key length of 2048 bits now, but many organizations have been using keys of length 4096 bits
    *

* How to test a given
  number n for being prime? Use Fermat’s little theorem. Take A ≠ 0,±1modn. If
  A n−1 ≠ 1modn, (4.47)
  then n is composite; otherwise, it is prime with high probability (but not for sure!). Repeat
  for many As to increase the likelihood of p,q being prime.
* RSA without padding is also called Textbook RSA
* We can fix a few issues by introducing padding.

  Malleability: If we have a strict format for messages, i.e. that the first or last bytes contain a specific value, simply multiplying both message and ciphertext will decrease the probability of creating a valid (in terms of padding) message.

  Semantical Security: Add randomness such that RSA is not deterministic anymore (a deterministic encryption scheme yields always the same x for each instance of x=encpubkey(m) for constant m and pubkey). See OAEP as an example on how to achieve this.
* For example RSA Encryption padding is randomized, ensuring that the same message encrypted multiple times looks different each time.
* While OAEP uses a one-way function on the plaintext, it's not quite a hash: it's called a mask generation function (MGF), and unlike a hash it can produce as much or as little output as you want (the output length is an argument to the function, and input length is decoupled from output length). This output should be pseudorandom.
    * You use this in a construction called a Feistel network. To pad a message to length k (so you're ultimately passing k bits through textbook RSA), you create two blocks. One of them (the seed S) is a short fixed-length random string; the other (the data block D) is longer, and includes the message and some more conventional padding (details aren't important) to make it so the two blocks have a combined length of k.

      Once you have these blocks, it's time to use your MGF. You first run MGF on S to get a mask as long as D; you XOR that mask with D to get D′. You then run MGF on D′ to get a mask as long as S, and XOR that with S to get S′. Concatenate D′ and S′ to get the thing you pass through RSA.

      On the other end, you decrypt to get D′ and S′. You first recover S by computing MGF(D′) and XORing with S′ (this is exactly what you did to get S′; it works just as well in reverse). Once you have S, you XOR D′ with MGF(S) to get D, and once you have D you take off the normal padding to get the message.
    * Feistel networks are generally a good way to turn a one-way function into a reversible function. In general, you divide the thing you're processing into two blocks, A1 and B1 (these have fixed length). You then compute Ai+1=Ai⊕f(Bi) and Bi+1=Bi⊕f(Ai+1), and repeat till you have An,Bn as your output. To reverse it, you compute Bi−1=Bi⊕f(Ai) and Ai−1=Ai⊕f(Bi−1), and repeat until you have A1,B1 again. This construction is used in several block ciphers; there, f also takes a subkey as input, so without the subkey you can't reverse the network. It's especially nice in hardware; a Feistel network means encryption and decryption are almost identical (you basically just reverse the order of the subkeys).
* Briefly, as you've certainly observed the RSA primitive is based on modular exponentiation and this operation is homomorphic in the sense that if someone knows two ciphertexts c1=md1 and c2=md2, then he can immediately deduce : (m1.m2)d=c1.c2=c. This is the essential reason to break this "homomorphism", by adding inside the plaintext some hash or other techniques.
* Using the word padding for RSA is by now rather incorrect - it's basically still called "padding" for historical reasons. The padding schemes for RSA did simply extend the message before converting a number. But newer schemes actually alter the message itself as well; the message cannot directly be identified in the number before exponentiation by RSA. OAEP is one of the schemes where the entire message is randomly transformed before RSA modular exponentiation.
* RSA is named after its (public) inventors, Ron Rivest, Adi Shamir,
  and Leonard Adleman
* setup: Let p and q be large primes, let N = pq, and let e and c be integers
*  Problem. Solve the congruence x e ≡ c (mod N) for the variable x
* Easy. Bob, who knows the values of p and q, can easily solve for x as
  described in Proposition 3.5.
*  Hard. Eve, who does not know the values of p and q, cannot easily find x.
* Dichotomy. Solving x e ≡ c (mod N) is easy for a person who possesses
  certain extra information, but it is apparently hard for all other people.
* d - decryption exponent
* e -  encryption exponent
*  It is clear that encryption can be done
  more efficiently if the encryption exponent e is a small number, and similarly,
  decryption is more efficient if the decryption exponent d is small
  * Sometimes the exponent is exponent 3, which is subject to an attack we’ll describe below [1]. (The most common exponent is 65537.)
  * This is an attack on “textbook” RSA because the weakness in this post could be avoiding by real-world precautions such as adding random padding to each message so that no two recipients are sent the exact same message.
  * By the way, a similar trick works even if you only have access to one encrypted message. Suppose you’re using a 2048-bit modulus N and exchanging a 256-bit key. If you message m is simply the key without padding, then m³ is less than N, and so you can simply take the cube root of the encrypted message in the integers.
    * If you use random padding such as OAEP in PKCS#1, most (all?) of the known weaknesses from using low exponents are no longer relevant.
  * Key length has a much higher practical impact on security. A 768-bit RSA key has been cracked recently (this was not easy ! Four years of work with big computers and bigger brains). A 1024-bit key is deemed adequate for the short term, but long-term uses (e.g. the encrypted data has high value and must still be confidential in year 2030) would mandate something bigger, e.g. 2048 bits.

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


