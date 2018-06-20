package example

import java.time.LocalDateTime

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import example.TRoot.mapper

@JsonTypeInfo(use = Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes(Array(
  new Type(value = classOf[A]),
  new Type(value = classOf[B]),
  new Type(value = classOf[C])
))
sealed trait TRoot {
  val rootString: String
  val rootDateTime: LocalDateTime
  val rootBigDecimal: BigDecimal
  val rootBoolean : Boolean

  def toJson: String = {
    mapper.writeValueAsString(this)
  }
}

object TRoot {
  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(new JavaTimeModule())
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

  private[example] def fromJson[T <: TRoot](json: String): T = {
    // Due to type erasure, need to provide
    // an instance of TypeReference with all the needed Type information.
    // @see https://stackoverflow.com/a/29824075/519951
    mapper.readValue(json, new TypeReference[T] {})
  }
}

sealed trait TLevelOne extends TRoot {
  val l1String: String
}

sealed trait TLevelTwo extends TLevelOne {
  val l2String: String
}

final case class A(
                    override val rootString: String,
                    override val rootDateTime: LocalDateTime,
                    override val rootBigDecimal: BigDecimal,
                    override val rootBoolean: Boolean
                  ) extends TRoot

object A {
  def fromJson(json: String): A = {
    TRoot.fromJson[A](json)
  }
}

final case class B(
                    override val rootString: String,
                    override val rootDateTime: LocalDateTime,
                    override val rootBigDecimal: BigDecimal,
                    override val rootBoolean: Boolean,
                    override val l1String: String
                  ) extends TLevelOne

object B {
  def fromJson(json: String): B = {
    TRoot.fromJson[B](json)
  }
}

final case class C(
                    override val rootString: String,
                    override val rootDateTime: LocalDateTime,
                    override val rootBigDecimal: BigDecimal,
                    override val rootBoolean: Boolean,
                    override val l1String: String,
                    override val l2String: String
                  ) extends TLevelTwo

object C {
  def fromJson(json: String): C = {
    TRoot.fromJson[C](json)
  }
}
