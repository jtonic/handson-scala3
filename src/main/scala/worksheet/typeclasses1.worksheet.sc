case class Person(name: String, age: Int)

// scala 3
extension (person: Person)
  def info: String = s"${person.name} | ${person.age}"
  def getOlder() = Person(person.name, person.age + 1)
  def nickname: String = if person.age >= 50 then "oldie" else "youngie"

val tony = Person("Tony", 53)

tony.info
tony.getOlder()
tony.nickname


// -----------------------------------------------------------------------------------
// type classes
// -----------------------------------------------------------------------------------

trait JsonWriter[T]:
  def write(value: T): String

def toJson[T](value: T)(using writer: JsonWriter[T]): String =
  writer.write(value)


given JsonWriter[Person] with
  def write(value: Person): String =
    s"""{"name": "${value.name}, "age": "${value.age}"}"""

val json: String = toJson(tony)

json

// -----------------------------------------------------------------------------------
// multiversal equality
// -----------------------------------------------------------------------------------

case class Apple(size: Int) derives CanEqual // idiomatic
// not idiomatic
// object Apple:
//   given CanEqual[Apple, Apple] = CanEqual.derived

case class Orange(size: Int)

val apple1 = Apple(1)
val apple2 = Apple(1)
val orange1 = Orange(1)

apple1 == apple2
// apple1 == orange1 // it is allowed for back if not strict equality

import scala.language.strictEquality
// apple1 == orange1 // it is not allowed with strict equality. compiler error

apple1 == apple2
