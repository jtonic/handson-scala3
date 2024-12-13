import scala.language.experimental.namedTuples

type Person = (name: String, age:  Int)
val tony: Person = (name: "Tony", age: 54)

@main def main =
  println(s"Hello $tony")
