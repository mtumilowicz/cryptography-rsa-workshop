package rsa.conversion

import java.nio.charset.StandardCharsets
import java.util.Base64

object EncodingService {

  def base64Encode(message: String): BigInt =
    BigInt(Base64.getEncoder.encode(message.getBytes(StandardCharsets.UTF_8)))

  def encode(message: String, symbols: Array[Char]): BigInt = {
    val base = symbols.length
    val indexes = symbols.zipWithIndex.toMap
    message.foldLeft(BigInt(0)) { case (value, c) => value * base + indexes(c) }
  }

}
