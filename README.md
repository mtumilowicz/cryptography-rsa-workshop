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