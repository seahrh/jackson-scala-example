package example

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, JsonSerializer, SerializerProvider}
import com.typesafe.scalalogging.LazyLogging

@JsonSerialize(using = classOf[WeekdaySerializer])
@JsonDeserialize(using = classOf[WeekdayDeserializer])
sealed trait Weekday extends Ordered[Weekday] {
  val name: String
  val abbreviation: String
  val order: Int

  override def compare(that: Weekday): Int = this.order - that.order
}

case object Monday extends Weekday {
  override val name: String = "Monday"
  override val abbreviation: String = "Mo"
  override val order: Int = 0
}
case object Tuesday extends Weekday {
  override val name: String = "Tuesday"
  override val abbreviation: String = "Tu"
  override val order: Int = 1
}
case object Wednesday extends Weekday {
  override val name: String = "Wednesday"
  override val abbreviation: String = "We"
  override val order: Int = 2
}
case object Thursday extends Weekday {
  override val name: String = "Thursday"
  override val abbreviation: String = "Th"
  override val order: Int = 3
}
case object Friday extends Weekday {
  override val name: String = "Friday"
  override val abbreviation: String = "Fr"
  override val order: Int = 4
}
case object Saturday extends Weekday {
  override val name: String = "Saturday"
  override val abbreviation: String = "Sa"
  override val order: Int = 5
}
case object Sunday extends Weekday {
  override val name: String = "Sunday"
  override val abbreviation: String = "Su"
  override val order: Int = 6
}


class WeekdaySerializer extends JsonSerializer[Weekday] {
  override def serialize(
                          w: Weekday,
                          json: JsonGenerator,
                          provider: SerializerProvider
                        ): Unit = {
    json.writeString(w.name)
  }
}

class WeekdayDeserializer extends JsonDeserializer[Weekday] with LazyLogging {
  override def deserialize(p: JsonParser, ctxt: DeserializationContext): Weekday = {
    import com.fasterxml.jackson.databind.JsonNode
    val node: JsonNode = p.getCodec.readTree(p)
    val s: String = node.asText()
    logger.debug(s"s=$s")
    s match {
      case Monday.name => Monday
      case Tuesday.name => Tuesday
      case Wednesday.name => Wednesday
      case Thursday.name => Thursday
      case Friday.name => Friday
      case Saturday.name => Saturday
      case Sunday.name => Sunday
      case _ => throw new IllegalArgumentException(
        s"[$s] is not a valid value for Weekday"
      )
    }
  }
}

