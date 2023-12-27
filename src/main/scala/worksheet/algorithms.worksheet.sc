// -----------------------------------------------------------------------------------
// Compute the sum of two integers without using + or - operators only on each of them
// -----------------------------------------------------------------------------------
def sum(a: Int, b: Int): Int =
  if b == 0 then a else sum(a + 1, b - 1)

sum(10, 20)

// -----------------------------------------------------------------------------------
// Insertion Sort Algorithm
// -----------------------------------------------------------------------------------
def isort(xs: List[Int]): List[Int] =
  if xs.isEmpty then Nil
  else insert(xs.head, isort(xs.tail))

def insert(x: Int, xs: List[Int]): List[Int] =
  if xs.isEmpty || x <= xs.head then x :: xs
  else xs.head :: insert(x, xs.tail)

isort(List(5, 3, 12, 1, 2))
