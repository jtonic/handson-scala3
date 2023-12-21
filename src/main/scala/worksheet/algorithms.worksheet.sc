// -----------------------------------------------------------------------------------
// Compute the sum of two integers without using + or - operators only on each of them
// -----------------------------------------------------------------------------------
def sum(a: Int, b: Int): Int =
  if b == 0 then a else sum(a + 1, b - 1)

sum(10, 20)
