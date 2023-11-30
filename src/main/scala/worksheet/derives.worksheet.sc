import scala.deriving.Mirror

// Define a type class
trait Show[T] {
  def show(value: T): String
}

// Companion object for the type class with a method to derive instances
object Show {
  inline given derived[T](using m: Mirror.Of[T]): Show[T] =
    new Show[T] {
      def show(value: T): String =
        inline m match {
          case p: Mirror.ProductOf[T] =>
            val product = value.asInstanceOf[Product]
            val fieldValues = product.productElementNames
              .zip(product.productIterator)
              .map { case (name, value) => s"$name = $value" }
              .mkString(", ")
            s"${product.productPrefix}($fieldValues)"
        }
    }
}

def show[T](value: T)(using writer: Show[T]): String =
  writer.show(value)


// A sample Person case class that derives Show
case class Person(name: String, age: Int) derives Show

// Usage
val tony = Person("Tony", 25)
val showPerson = summon[Show[Person]] // Retrieve the given instance
println(showPerson.show(tony)) // Output will be a string representation of `tony`


case class Animal(val name: String, group: String, liveLong: Int) derives Show

val cat = Animal("Catie", "cats", 15)
val showCat = summon[Show[Animal]]
println(showCat.show(cat))


//todo: implement he real logic here
println(show(cat))
println(show(tony))
