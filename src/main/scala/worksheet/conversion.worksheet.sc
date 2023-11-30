import ro.jtonic.handson.scala3.conference._

val tony = Person("1", "Antonel Pazargic", Gender.Male, 53)
val irina = Person("2", "Irina Stan", Gender.Female, 33)
val liviu = Person("3", name = "Liviu Pazargic", gender = Gender.Male, age = 41)
val roxana = Person("4", "Roxana Adascalului", Gender.Female, age = 37)

val people = List(irina, tony)

// Conversion trait to convert Person to String
trait PersonToStringConversion extends Conversion[Person, String]:
  def apply(person: Person): String = s"${person.name} ${person.age}"

// Extension function to convert Person to String
extension (person: Person)
  def toString: String = summon[PersonToStringConversion].apply(person)

// Given instance of PersonToStringConversion
given PersonToStringConversion = new PersonToStringConversion {}

// Example usage
val tony1 : String = Person("Antonel", "Pazargic", Gender.Male, 53)
println(tony1)
