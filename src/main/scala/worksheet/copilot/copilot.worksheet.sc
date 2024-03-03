// ------------------------------------------------------------------------
// algormithm to find the missing number in a sequence of natural numbers
// ------------------------------------------------------------------------
val numbers: List[Int] = (1 to 10).toList

val randomIndex = scala.util.Random.nextInt(numbers.length)
val updatedNumbers = numbers.patch(randomIndex, Nil, 1)
def findMissingNumber(numbers: List[Int]): Option[Int] =
  val expectedSum = (numbers.length + 1) * (numbers.length + 2) / 2
  val actualSum = numbers.sum
  val missingNumber = expectedSum - actualSum
  if (missingNumber >= 1 && missingNumber <= numbers.length + 1) Some(missingNumber)
  else None

val missingNumber = findMissingNumber(updatedNumbers)
println(s"The missing number is: $missingNumber")


// ------------------------------------------------------------------------
// algorithm to find the number of handshakes in a room
// ------------------------------------------------------------------------
def computeHandshakes(people: Int): Int =
  if (people <= 1) 0
  else people * (people - 1) / 2

val numberOfPeople = 10
val numberOfHandshakes = computeHandshakes(numberOfPeople)
println(s"The number of handshakes is: $numberOfHandshakes")
