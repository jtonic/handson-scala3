//-----------------------------------------------------------------------------
// partially applied functions
//-----------------------------------------------------------------------------
// https://docs.scala-lang.org/tour

def sum(a: Int, b: Int, c: Int): Int = a + b + c

sum(1, 2, 3)

val a = sum(_, _, _)
a(1, 2, 3)

val inc = sum(1, _, _)
inc(2, 3)

val addThree = sum(1, 2, _)
addThree(3)

addThree.getClass()

// -----------------------------------------------------------------------------------
// closures
// -----------------------------------------------------------------------------------
var more = 10
val addMore = (x: Int) => x + more

addMore(10)

more = 20
addMore(10)

def makeIncreaser(more: Int) = (x: Int) => x + more

val incOne = makeIncreaser(1)
val incTen = makeIncreaser(10)

incOne(10)
incTen(10)

// -----------------------------------------------------------------------------------
// SAM types
// -----------------------------------------------------------------------------------

trait Increaser:
  def increase(x: Int): Int

def increaseOne(increaser: Increaser): Int =
  increaser.increase(1)

increaseOne(new Increaser {
  def increase(x: Int): Int = x + 7
})

increaseOne((x: Int) => x + 7)
increaseOne(x => x + 7)
increaseOne(_ + 7)
