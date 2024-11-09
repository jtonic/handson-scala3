import org.scalatest.matchers.should.Matchers._

// --------------------------------------------------------------------------
// binary operators algorithms
// --------------------------------------------------------------------------

extension (n: Int)
  def asBinaryString: String = n.toBinaryString.reverse.padTo(32, '0').reverse
  def isPowerOfTwo = n > 0 && (n & (n - 1)) == 0
  def isEven = (n & 1) == 0
  def isOdd = (n & 1) == 1


// having two numbers
val a = 60
a.asBinaryString

val b = 13
b.asBinaryString

val c = 16
c.asBinaryString

// --------------------------------------------------------------------------
// invert the bits
// --------------------------------------------------------------------------
val r0 = ~a
 a.asBinaryString
r0.asBinaryString

val r01 = c - 1
  c.asBinaryString
r01.asBinaryString
val r02 = c & r01
r02.asBinaryString

// --------------------------------------------------------------------------
// multiply by 2^n: shift left
// --------------------------------------------------------------------------
val r1 = a << 2
r1.toBinaryString

// --------------------------------------------------------------------------
// divide by 2^n: shift right
// --------------------------------------------------------------------------
val r2 = a >> 1
r2.toBinaryString


// --------------------------------------------------------------------------
// check if a number is even or odd
// --------------------------------------------------------------------------
a.isEven should be (true)
b.isEven should be (false)

3.isOdd shouldBe true
4.isOdd shouldBe false
4.isEven shouldBe true

// --------------------------------------------------------------------------
// check if a number is a power of 2
// --------------------------------------------------------------------------
a.isPowerOfTwo
b.isPowerOfTwo
c.isPowerOfTwo

// --------------------------------------------------------------------------
// check if the k-th bit is set
// --------------------------------------------------------------------------

extension (self: Int)
  def hasBitSet(bit: Byte) =
    val mask: Int = Math.pow(2, bit - 1).toInt
    (self & mask) == mask

//later: as a conversion (from binary string representation to Int)
val val1: Int = Integer.parseInt("01000000", 2)
val val2: Int = Integer.parseInt("11000000", 2)
val val3: Int = Integer.parseInt("11100000", 2)
val val4: Int = Integer.parseInt("10000000", 2)

List(val1, val2, val3).foreach{ _.hasBitSet(7) should be (true) }
val4.hasBitSet(7) should be (false)
