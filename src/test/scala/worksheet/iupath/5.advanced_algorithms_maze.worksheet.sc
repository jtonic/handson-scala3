import org.scalatest.matchers.should.Matchers.*


val size = 8

val t = true
val f = false

val maze = Array(
  Array( t, f, t, f, f, t, t, t ),
  Array( t, t, t, t, t, f, t, f ),
  Array( t, t, f, t, t, f, t, t ),
  Array( f, t, t, f, t, f, f, t ),
  Array( f, t, t, t, t, t, t, t ),
  Array( t, f, t, f, t, f, f, t ),
  Array( t, t, t, t, t, t, t, t ),
  Array( f, t, f, f, f, t, f, t )
)

def printArray(matrix: Array[Array[Boolean]], inverse: Boolean = false): Unit =
  val hit = if inverse then "x" else "-"
  val missed = if inverse then "-" else "x"
  for i <- 0 until matrix.size do
    for j <- 0 until matrix(i).size do
      print(s"${if matrix(i)(j) then hit else missed}")
    println()

val solution: Array[Array[Boolean]] = Array.ofDim(8, 8)

def go(row: Int, col: Int): Boolean =
  if  row == size - 1
      && col == size - 1
      && maze(row)(col)
  then
    solution(row)(col) = true
    true
  else if row >= 0 && row < size &&
          col >= 0 && col < size &&
          maze(row)(col)
  then
    if solution(row)(col) then false
    else
      solution(row)(col) = true
      if go(row + 1, col) then true
      else if go(row, col + 1) then true
      else if go(row - 1, col) then true
      else if go(row, col - 1) then true
      else
        solution(row)(col) = false
        false
  else false

printArray(maze)
if go(0, 0) then printArray(solution, inverse = true)
