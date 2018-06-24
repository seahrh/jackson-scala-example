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
  val l2DateTime: LocalDateTime
  val l2BigDecimal: BigDecimal
  val l2Boolean: Boolean
  // As of jackson 2.9.5,
  // deserialize Seq works but Array does not work.
  val l2Seq: Seq[String]
  val l2Tuple: (String, Long, BigDecimal)
  val l2LongMax: Long = Long.MaxValue
  val l2LongMin: Long = Long.MinValue
  // As of jackson 2.9.5, the following annotation did not work for Double.
  // Known issue for jackson scala module in use with case class.
  // @see https://github.com/FasterXML/jackson-module-scala/issues/354
  // @JsonSerialize(using = classOf[ToStringSerializer])
  // Solution: use BigDecimal instead of double, float.
  // Also use BigDecimal rounding to control precision.
  val l2MaxDoubleAsBigDecimal: BigDecimal = BigDecimal(Double.MaxValue)
  val l2MinDoubleAsBigDecimal: BigDecimal = BigDecimal(Double.MinValue)
  val l2Null: String = null
  val l2EmptyArray: Array[String] = Array()
  val l2OptionSome: Option[String] = Option("some")
  val l2OptionNone: Option[String] = None
  // As of jackson 2.9.5, tuple with Option elements can be serialized
  // but deserialization will fail with
  // com.fasterxml.jackson.databind.exc.MismatchedInputException:
  // Cannot deserialize instance of `java.lang.String` out of VALUE_NULL token
  // e.g.
  // val rootTupleWithOptions: (Option[String], Option[String]) = (Option("some"), None)
  val l2Weekday: Weekday
  val l2WeekdayOption: Option[Weekday] = Option(Monday)
  val l2WeekdayOptionNone: Option[Weekday] = None
  val l2WeekdaySeq: Seq[Weekday]
}

final case class A(
                    override val rootString: String
                  ) extends TRoot

object A {
  def fromJson(json: String): A = {
    TRoot.fromJson[A](json)
  }
}

final case class B(
                    override val rootString: String,
                    override val l1String: String
                  ) extends TLevelOne

object B {
  def fromJson(json: String): B = {
    TRoot.fromJson[B](json)
  }
}

final case class C(
                    override val rootString: String,
                    override val l1String: String,
                    override val l2String: String,
                    override val l2DateTime: LocalDateTime,
                    override val l2BigDecimal: BigDecimal,
                    override val l2Boolean: Boolean,
                    override val l2Seq: Seq[String],
                    override val l2Tuple: (String, Long, BigDecimal),
                    override val l2Weekday: Weekday,
                    override val l2WeekdaySeq: Seq[Weekday]
                  ) extends TLevelTwo

object C {
  def fromJson(json: String): C = {
    TRoot.fromJson[C](json)
  }
}
