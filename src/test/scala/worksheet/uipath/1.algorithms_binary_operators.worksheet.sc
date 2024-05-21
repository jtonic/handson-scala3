import org.scalatest.matchers.should.Matchers._

// --------------------------------------------------------------------------
// binary operators algorithms
// --------------------------------------------------------------------------

extension (n: Int)
  def asBinaryString: String = n.toBinaryString.reverse.padTo(32, '0').reverse
  def isEven = (n & 1) == 0
  def isOdd = (n & 1) == 1
  def isPowerOfTwo = n > 0 && (n & (n - 1)) == 0

// having two numbers
val a = 60
a.asBinaryString

val b = 13
b.asBinaryString

val c = 16
c.asBinaryString

// invert the bits
val r0 = ~a
 a.asBinaryString
r0.asBinaryString

val r01 = c - 1
  c.asBinaryString
r01.asBinaryString
val r02 = c & r01
r02.asBinaryString

// bit shift left (multiply by 2^n)
val r1 = a << 2
r1.toBinaryString

// bit shift right (divide by 2^n)
val r2 = a >> 1
r2.toBinaryString

// check if a number is even or odd
a.isEven
b.isEven

// check if a number is a power of 2
a.isPowerOfTwo
b.isPowerOfTwo
c.isPowerOfTwo


3.isOdd shouldBe true
4.isOdd shouldBe false
4.isEven shouldBe true
