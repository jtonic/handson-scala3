import scala.util.Failure
import scala.util.Success
import scala.util.Try

case class PosInt(value: Int):
  require(value > 0)
  export value.{<< as bls, >> as brs,  >>> as _, - as _, *}
  def inc = value + 1
  def - (other: Int): Try[Int] =
    (value - other) match {
      case diff if diff >= 0 => Success(diff)
      case _ => Failure{
        new IllegalArgumentException(s"Invalid subtraction: $value - $other")}
    }


val i1 = PosInt(10)
i1 + 3
i1 * 2

i1 bls 1
i1 brs 1
i1.inc

i1 - 3
i1 - 20


trait Animal:
  def eat = println("Animal is eating")
  def die = println("Animal is dying")
  def sleep = println("Animal is sleeping")

class Mammal(animal: Animal) extends Animal:
  export animal.{die as _, *}
  override def die = println("Mammal is dying")

val mammal: Animal = Mammal(new Animal {})
mammal.eat
mammal.die
mammal.sleep
