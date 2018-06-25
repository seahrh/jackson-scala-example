# jackson-scala-example
Serde = Serialization/Deserialization

Goals
1. Evaluate [Jackson Scala module](https://github.com/FasterXML/jackson-module-scala)'s capability to serde case classes and collections in Scala

Jackson examples based on a scala trait hierarchy tree
1. Root and intermediate nodes are traits
1. Leaf nodes are case classes
1. Root starts at level zero of the tree. The examples use a tree that is four levels deep
   1. see [Root](src/main/scala/example/Root.scala)

[Tested](src/test/scala/example/RootSpec.scala) on Jackson Scala module **v2.9.5**.

## Notes
1. Works out of the box with Scala types like `String`, `Boolean`, `Long`, `BigDecimal`
1. Case classes are supported
   1. see [Money](src/main/scala/example/Money.scala)
1. Subtypes are supported, with additional configuration e.g.
   ```
   @JsonTypeInfo(use = Id.NAME,
     include = JsonTypeInfo.As.PROPERTY,
     property = "type")
   @JsonSubTypes(Array(
     new Type(value = classOf[A]),
     new Type(value = classOf[B]),
     new Type(value = classOf[C])
   ))
   ```
1. Write custom serializer/deserializer for sealed case objects, which is a way to define enumerations in Scala
   1. see [Weekday](src/main/scala/example/Weekday.scala)
1. Scala `Option`
   1. `None` is serialized as JSON null
   1. Configuration allows for other ways to handle `None` e.g. to omit the field in serialization
   1. If value is present, only the value is serialized (without the `Option` wrapper)
1. Scala collections
   1. Tuples and `Seq` are serialized as JSON array. Hence an empty tuple or `Seq` is serialized as an empty array `[]`
   1. Supports tuples of mixed types
   1. `Map` is serialized as JSON object. Hence an empty `Map` is serialized as an empty object `{}`
1. Java 8's time package `java.time.*` requires additional dependency to serde in ISO string format
   ```scala
   import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
   
   mapper.registerModule(new JavaTimeModule())
   ```
1. General serde tips not specific to Jackson
   1. Prefer `BigDecimal` to `Double`/`Float` because precision can be controlled
## Limitations
1. Deserialize `Seq` works but not `Array`
1. Tuple with Option elements can be serialized but deserialization will fail with
   ```
   com.fasterxml.jackson.databind.exc.MismatchedInputException:
     Cannot deserialize instance of `java.lang.String` out of VALUE_NULL token
   ```
1. [Known issue](https://github.com/FasterXML/jackson-module-scala/issues/354) that the `@JsonSerialize ` annotation may not work at the attribute level
## References
1. [Sealed case objects as enumerations in Scala](https://pedrorijo.com/blog/scala-enums/)
1. [Custom deserialization in Jackson](http://www.baeldung.com/jackson-deserialization)
1. [json.org](https://www.json.org/)
