package example

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class RootSpec extends FlatSpec with LazyLogging {
  "Class A" should "serialize to json" in {
    val a: A = A("root=a")
    val actual: String = a.toJson
    logger.debug(s"actual=$actual")
    assertResult(
      "{\"type\":\"A\",\"rootString\":\"root=a\"}"
    )(actual)
  }

  it should "deserialize json" in {
    val e: A = A("root=a")
    val a: A = A.fromJson(e.toJson)
    assertResult(e)(a)
  }

  "Class B" should "serialize to json" in {
    val b: B = B("root=b", "l1=b")
    assertResult(
      "{\"type\":\"B\",\"rootString\":\"root=b\",\"l1String\":\"l1=b\"}"
    )(b.toJson)
  }

  it should "deserialize json" in {
    val e: B = B("root=b", "l1=b")
    val a: B = B.fromJson(e.toJson)
    assertResult(e)(a)
  }

  "Class C" should "serialize to json" in {
    val c: C = C("root=c", "l1=c", "l2=c")
    assertResult(
      "{\"type\":\"C\",\"rootString\":\"root=c\",\"l1String\":\"l1=c\",\"l2String\":\"l2=c\"}"
    )(c.toJson)
  }

  it should "deserialize json" in {
    val e: C = C("root=c", "l1=c", "l2=c")
    val a: C = C.fromJson(e.toJson)
    assertResult(e)(a)
  }
}
