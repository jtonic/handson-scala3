
enum Gender:
  case Male, Female

case class Person(firstName: String, lastName: String, gender: Gender, age: Int)

val tony = Person("Antonel", "Pazargic", Gender.Male, 53)
val irina = Person("Irina", "Stan", Gender.Female, 33)
val liviu = Person(firstName = "Liviu", lastName = "Pazargic", gender = Gender.Male, age = 41)
val roxana = Person("Roxana", "Adascalului", Gender.Female, age = 37)

val people = List(irina, tony)

// Conversion trait to convert Person to String
trait PersonToStringConversion extends Conversion[Person, String]:
  def apply(person: Person): String = s"${person.firstName} ${person.lastName}"

// Extension function to convert Person to String
extension (person: Person)
  def toString: String = summon[PersonToStringConversion].apply(person)

// Given instance of PersonToStringConversion
given PersonToStringConversion = new PersonToStringConversion {}

// Example usage
val tony1 : String = Person("Antonel", "Pazargic", Gender.Male, 53)
println(tony1)
