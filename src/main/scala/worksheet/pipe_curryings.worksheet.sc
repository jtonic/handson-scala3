// ==========================================
// 1. piping through function composition
// 2. currying
// ==========================================
import ro.jtonic.handson.scala3.util.function.{<<, >>, |>, <|}

// 1.

def foo(x: Int) = x + 1
def boo(x: Int) = x + 2
def bee(x: Int) = x + 3

1 |> foo >> boo << bee

foo >> boo << bee <| 1

1 |> foo
  |> boo
  |> bee
  |> (_ + 1)

//2.

val sum: (Int, Int) => Int = (x, y) => x + y

val sumC = sum.curried

val sumU = Function.uncurried(sumC)

val inc = sumC(1)

inc(2)

sum(1, 2)
sumU(1, 2)
