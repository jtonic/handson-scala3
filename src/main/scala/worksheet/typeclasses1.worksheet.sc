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


// type classes

trait JsonWriter[T]:
  def write(value: T): String

def toJson[T](value: T)(using writer: JsonWriter[T]): String =
  writer.write(value)


given JsonWriter[Person] with
  def write(value: Person): String =
    s"""{"name": "${value.name}, "age": "${value.age}"}"""

val json: String = toJson(tony)

json
