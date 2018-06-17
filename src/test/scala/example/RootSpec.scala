package example

import org.scalatest.FlatSpec

class RootSpec extends FlatSpec {
  "Class A" should "serialize to json" in {
    val a: A = A("root=a")
    assertResult(
      "{\"rootString\":\"root=a\"}"
    )(a.toJson)
  }

  "Class B" should "serialize to json" in {
    val b: B = B("root=b", "l1=b")
    assertResult(
      "{\"rootString\":\"root=b\",\"l1String\":\"l1=b\"}"
    )(b.toJson)
  }

  "Class C" should "serialize to json" in {
    val c: C = C("root=c", "l1=c", "l2=c")
    assertResult(
      "{\"rootString\":\"root=c\",\"l1String\":\"l1=c\",\"l2String\":\"l2=c\"}"
    )(c.toJson)
  }
}
