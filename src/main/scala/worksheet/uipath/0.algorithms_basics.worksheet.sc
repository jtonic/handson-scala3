import scala.annotation.tailrec
// --------------------------------------------------------------
// 1. compute the fibonacci sequence
// --------------------------------------------------------------
def fib(n: Int): Int = {
  if (n <= 1) n
  else fib(n - 1) + fib(n - 2)
}

fib(10)

// fibonacci sequence using tail recursion
def fibTail(n: Long): Long = {
  @tailrec
  def loop(n: Long, prev: Long, cur: Long): Long = {
    if (n <= 0) prev
    else loop(n - 1, cur, prev + cur)
  }

  loop(n, 0, 1)
}

fibTail(10)

// fibonacci sequence using memoization
def fibMemo(n: Int): Int = {
  val memo = Array.fill(n + 1)(-1)
  def loop(n: Int): Int = {
    if (n <= 1) n
    else if (memo(n) != -1) memo(n)
    else {
      memo(n) = loop(n - 1) + loop(n - 2)
      memo(n)
    }
  }

  loop(n)
}
// fib(50)
fibTail(50)
fibMemo(50)

// --------------------------------------------------------------
// 2. Compute the factorial of a number using recursion
// --------------------------------------------------------------
def factorial(n: Int): Int = {
  if (n <= 0) 1
  else n * factorial(n - 1)
}

// compute factorial using tail recursion
def factorialTail(n: Int): Int = {
  @tailrec
  def loop(n: Int, acc: Int): Int = {
    if (n <= 0) acc
    else loop(n - 1, n * acc)
  }

  loop(n, 1)
}

factorial(5)
factorialTail(5)
