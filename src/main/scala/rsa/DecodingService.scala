package rsa

import java.util.Base64
import scala.annotation.tailrec

object DecodingService {

  def base64decode(message: BigInt): String =
    new String(Base64.getDecoder.decode(message.toByteArray))

  def decode(number: BigInt, symbols: Array[Char]): String = {
    val base = symbols.length

    @tailrec
    def loop(current: BigInt, decoded: String): String = {
      if (current == 0) decoded
      else {
        val symbolIndex = (current % base).toInt
        loop(current / base, symbols(symbolIndex) + decoded)
      }
    }

    loop(number, "")
  }

}
