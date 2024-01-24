

val aString = "I love Scala"

val meaningOfLife = 42

if meaningOfLife > 40 then "I love Scala" else "I hate Scala"

// a code block is also an expression
val codeValue = {
  val aLocalValue = 10
  aLocalValue + 3
}

def factorial(n: Int): Int =
  if n == 1
  then 1
  else n * factorial(n -1)

factorial(5)

val unit = ()
