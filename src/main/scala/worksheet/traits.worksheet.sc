import os.copy.over

trait Philosopher:
  def philosophize(): Unit = println("I consume memory, therefore I am!")

class Animal(val name: String):
  override def toString = name

class Frog(name: String) extends Animal(name) with Philosopher

val tony = Frog("Tony")

tony.philosophize()

class Cat(name: String) extends Animal(name), Philosopher:
  override def philosophize(): Unit = println("I am cat so I don't philosophize")

val fluffy = Cat("Fluffy")
fluffy.philosophize()
