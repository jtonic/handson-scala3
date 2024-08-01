import monocle.law.SetterLaws
import slick.ast.JoinType.Outer
class Food

abstract class Animal {
  type SuitableFood <: Food
  def eat(food: SuitableFood): Unit
}

class Grass extends Food
class Cow extends Animal:
  type SuitableFood = Grass
  def eat(food: Grass) =
    println("Cow eat grass")

class Fish extends Food
class Dolphin extends Animal:
  type SuitableFood = Fish
  def eat(food: Fish) =
    println("Dolphin eat fish")

class DogFood extends Food
class Dog extends Animal:
  type SuitableFood = DogFood
  def eat(food: DogFood) =
    println("Dog eat dog food")

val cow = Cow()
cow.eat(Grass())

val dog = Dog()
dog.eat(DogFood())
cow.eat(new cow.SuitableFood) // error: type mismatch
// dog.eat(new cow.SuitableFood) // error: type mismatch
val lessie = Dog()
lessie.eat(new dog.SuitableFood)

val dolphin = Dolphin()
dolphin.eat(Fish())
// dolphin.eat(Grass()) // error: type mismatch

class Outer:
  class Inner:
    def f(i: Inner) = println("f")
    def g(i: Outer#Inner) = println("g")
  def h(i: Outer#Inner) = println("h")

val o1 = Outer()
val o2 = Outer()

val i1 = o1.Inner()
val i2 = o2.Inner()

// i1.f(i2) // error: type mismatch
i1.g(i2)

// new Outer#Inner // is not an path-dependent type

// ------------------------------------------------------------------------
// 4. Refinement Types
// ------------------------------------------------------------------------

class Pasture:
  var animals: List[Animal { type SuitableFood = Grass }] = Nil

val pasture = Pasture()
val cow1 = Cow()
val cow2 = Cow()
pasture.animals = List(cow1, cow2)
