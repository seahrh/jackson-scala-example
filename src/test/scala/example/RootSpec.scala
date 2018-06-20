package example

import java.time.LocalDateTime

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class RootSpec extends FlatSpec with LazyLogging {
  "Class A" should "serialize to json" in {
    // datetime string to millisecond precision
    val dts: String = "2018-01-01T14:59:59.999"
    val a: A = A(
      rootString = "root=a",
      rootDateTime = LocalDateTime.parse(dts),
      rootBigDecimal = BigDecimal(1.23456789),
      rootBoolean = false
    )
    val actual: String = a.toJson
    logger.debug(s"actual=$actual")
    assertResult(
      s"""{"type":"A","rootString":"root=a","rootDateTime":"$dts","rootBigDecimal":1.23456789,"rootBoolean":false}"""
    )(actual)
  }

  it should "deserialize json" in {
    // datetime string to millisecond precision
    val dts: String = "2018-01-01T14:59:59.999"
    val e: A = A(
      rootString = "root=a",
      rootDateTime = LocalDateTime.parse(dts),
      rootBigDecimal = BigDecimal(1.23456789),
      rootBoolean = false
    )
    val a: A = A.fromJson(e.toJson)
    assertResult(e)(a)
  }

  "Class B" should "serialize to json" in {
    // datetime string to millisecond precision
    val dts: String = "2018-01-01T14:59:59.999"
    val b: B = B(
      rootString = "root=b",
      rootDateTime = LocalDateTime.parse(dts),
      rootBigDecimal = BigDecimal(1.23456789),
      rootBoolean = false,
      l1String = "l1=b"
    )
    assertResult(
      s"""{"type":"B","rootString":"root=b","rootDateTime":"$dts","rootBigDecimal":1.23456789,"rootBoolean":false,"l1String":"l1=b"}"""
    )(b.toJson)
  }

  it should "deserialize json" in {
    // datetime string to millisecond precision
    val dts: String = "2018-01-01T14:59:59.999"
    val e: B = B(
      rootString = "root=b",
      rootDateTime = LocalDateTime.parse(dts),
      rootBigDecimal = BigDecimal(1.23456789),
      rootBoolean = false,
      l1String = "l1=b"
    )
    val a: B = B.fromJson(e.toJson)
    assertResult(e)(a)
  }

  "Class C" should "serialize to json" in {
    // datetime string to millisecond precision
    val dts: String = "2018-01-01T14:59:59.999"
    val c: C = C(
      rootString = "root=c",
      rootDateTime = LocalDateTime.parse(dts),
      rootBigDecimal = BigDecimal(1.23456789),
      rootBoolean = false,
      l1String = "l1=c",
      l2String = "l2=c"
    )
    assertResult(
      s"""{"type":"C","rootString":"root=c","rootDateTime":"$dts","rootBigDecimal":1.23456789,"rootBoolean":false,"l1String":"l1=c","l2String":"l2=c"}"""
    )(c.toJson)
  }

  it should "deserialize json" in {
    // datetime string to millisecond precision
    val dts: String = "2018-01-01T14:59:59.999"
    val e: C = C(
      rootString = "root=c",
      rootDateTime = LocalDateTime.parse(dts),
      rootBigDecimal = BigDecimal(1.23456789),
      rootBoolean = false,
      l1String = "l1=c",
      l2String = "l2=c"
    )
    val a: C = C.fromJson(e.toJson)
    assertResult(e)(a)
  }
}
