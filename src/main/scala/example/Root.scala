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
  val rootBoolean: Boolean
  val rootLongMax: Long = Long.MaxValue
  val rootLongMin: Long = Long.MinValue
  // As of jackson 2.9.5, the following annotation did not work for Double.
  // Known issue for jackson scala module in use with case class.
  // @see https://github.com/FasterXML/jackson-module-scala/issues/354
  // @JsonSerialize(using = classOf[ToStringSerializer])
  // Solution: use BigDecimal instead of double, float.
  // Also use BigDecimal rounding to control precision.
  val rootMaxDoubleAsBigDecimal: BigDecimal = BigDecimal(Double.MaxValue)
  val rootMinDoubleAsBigDecimal: BigDecimal = BigDecimal(Double.MinValue)
  val rootNull: String = null
  val rootEmptyArray: Array[String] = Array()
  val rootOptionSome: Option[String] = Option("some")
  val rootOptionNone: Option[String] = None
  // As of jackson 2.9.5, deserialize Seq works but Array does not work.
  val rootSeq: Seq[String]
  val rootTuple: (String, Long, BigDecimal)
  // As of jackson 2.9.5, tuple with Option elements can be serialized
  // but deserialization will fail with
  // com.fasterxml.jackson.databind.exc.MismatchedInputException:
  // Cannot deserialize instance of `java.lang.String` out of VALUE_NULL token
  // e.g.
  // val rootTupleWithOptions: (Option[String], Option[String]) = (Option("some"), None)

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
                    override val rootBoolean: Boolean,
                    override val rootSeq: Seq[String],
                    override val rootTuple: (String, Long, BigDecimal)
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
                    override val rootSeq: Seq[String],
                    override val rootTuple: (String, Long, BigDecimal),
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
                    override val rootSeq: Seq[String],
                    override val rootTuple: (String, Long, BigDecimal),
                    override val l1String: String,
                    override val l2String: String
                  ) extends TLevelTwo

object C {
  def fromJson(json: String): C = {
    TRoot.fromJson[C](json)
  }
}
