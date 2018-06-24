package example

import java.time.LocalDateTime

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class RootSpec extends FlatSpec with LazyLogging {

  private val maxDoubleAsBigDecimal: BigDecimal = BigDecimal(Double.MaxValue)
  private val minDoubleAsBigDecimal: BigDecimal = BigDecimal(Double.MinValue)

  "Class A" should "serialize to json" in {
    val a: A = A(
      rootString = "root=a"
    )
    val actual: String = a.toJson
    logger.debug(s"actual=$actual")
    assertResult(
      s"""{"type":"A","rootString":"root=a"}"""
    )(actual)
  }

  it should "deserialize json" in {
    val e: A = A(
      rootString = "root=a"
    )
    val a: A = A.fromJson(e.toJson)
    assertResult(e)(a)
  }

  "Class B" should "serialize to json" in {
    val b: B = B(
      rootString = "root=b",
      l1String = "l1=b"
    )
    assertResult(
      s"""{"type":"B","rootString":"root=b","l1String":"l1=b"}"""
    )(b.toJson)
  }

  it should "deserialize json" in {
    val e: B = B(
      rootString = "root=b",
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
      l2DateTime = LocalDateTime.parse(dts),
      l2BigDecimal = BigDecimal(1.23456789),
      l2Boolean = false,
      l2Seq = Seq("one", "two", "three"),
      l2Tuple = ("hello", 1L, BigDecimal(1.2)),
      l1String = "l1=c",
      l2String = "l2=c",
      l2Weekday = Sunday
    )
    val actual: String = c.toJson
    logger.debug(s"actual=$actual")
    assertResult(
      s"""{"type":"C","rootString":"root=c","l1String":"l1=c","l2String":"l2=c","l2DateTime":"$dts","l2BigDecimal":1.23456789,"l2Boolean":false,"l2Seq":["one","two","three"],"l2Tuple":["hello",1,1.2],"l2Weekday":"Sunday","l2LongMax":${Long.MaxValue},"l2LongMin":${Long.MinValue},"l2MaxDoubleAsBigDecimal":$maxDoubleAsBigDecimal,"l2MinDoubleAsBigDecimal":$minDoubleAsBigDecimal,"l2Null":null,"l2EmptyArray":[],"l2OptionSome":"some","l2OptionNone":null}"""
    )(actual)
  }

  it should "deserialize json" in {
    // datetime string to millisecond precision
    val dts: String = "2018-01-01T14:59:59.999"
    val e: C = C(
      rootString = "root=c",
      l2DateTime = LocalDateTime.parse(dts),
      l2BigDecimal = BigDecimal(1.23456789),
      l2Boolean = false,
      l2Seq = Seq("one", "two", "three"),
      l2Tuple = ("hello", 1L, BigDecimal(1.2)),
      l1String = "l1=c",
      l2String = "l2=c",
      l2Weekday = Sunday
    )
    val a: C = C.fromJson(e.toJson)
    assertResult(e)(a)
  }
}
