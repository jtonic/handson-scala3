class Animal:
  val age: Int = 0
  def eat(): Unit = println("I'm eating")

class Dog(val name: String) extends Animal

val anAnimal = new Animal
val anAnimal2 = Animal()

val aDog = Dog("Lassie")
aDog.age
aDog.eat()
aDog.name

// 2. Inheritance. Traits as mixins, single class extension
trait Carnivore:
  def eat(animal: Animal): Unit

trait Philosopher:
  def ?!(thought: String): Unit

class Tiger extends Animal with Carnivore with Philosopher:
  override def ?!(thought: String): Unit = println(s"Thinking of '$thought'")
  def eat(animal: Animal): Unit = println(s"I'm a tiger and I'm eating an animal.")

val aTiger = Tiger()
aTiger.eat(aDog)
aTiger ?! "What if we can fly"

// 3. Anonymous classes
val aDinosaur = new Carnivore:
  def eat(animal: Animal): Unit = println("I can eat pretty much everything")
aDinosaur.eat(aDog)


// 4. Singleton objects

object MySingleton:
  val mySpecialValue = 12
  def mySpecialFoo(): Int = 20
  def apply(x: Int) = x + 1

MySingleton.mySpecialValue
MySingleton.mySpecialFoo()
MySingleton.apply(10)
MySingleton(20)

// 5. Generics

val aList = List(1, 2, 3)
aList.head
aList.tail
