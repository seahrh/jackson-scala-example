package example

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

sealed trait TRoot {
  val rootString: String

  def toJson: String = {
    TRoot.toJson(this)
  }
}

object TRoot {
  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  //mapper.registerModule(new JavaTimeModule())
  //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

  def toJson(obj: TRoot): String = {
    mapper.writeValueAsString(obj)
  }
}

sealed trait TLevelOne extends TRoot {
  val l1String: String
}

sealed trait TLevelTwo extends TLevelOne {
  val l2String: String
}

final case class A(
                    override val rootString: String
                  ) extends TRoot

final case class B(
                    override val rootString: String,
                    override val l1String: String
                  ) extends TLevelOne

final case class C(
                    override val rootString: String,
                    override val l1String: String,
                    override val l2String: String
                  ) extends TLevelTwo
