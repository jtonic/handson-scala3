// ------------------------------------------------------------------------
// 1. Abstract Members
// ------------------------------------------------------------------------

trait Abstract:
  type T
  def transform(x: T): T
  val initial: T
  var current: T

class Concrete extends Abstract:
  type T = String
  def transform(x: String) = x + x
  val initial = "hi"
  var current = initial


// ------------------------------------------------------------------------
// 2. Parameterized Traits
// ------------------------------------------------------------------------
trait RationalTrait(val numerArg: Int, val denomArg: Int):
  require(denomArg != 0)
  private val g = gcd(numerArg, denomArg)
  val numer = numerArg / g
  val denom = denomArg / g
  private def gcd(a: Int, b: Int): Int =
    if b == 0 then a else gcd(b, a % b)
  override def toString = s"$numer/$denom"

val x = 2
val rational = new RationalTrait(1, x) {}

object TwoThirds extends RationalTrait(2, 3)

class RationalClass(n: Int, d: Int) extends RationalTrait(n, d):
  def + (that: RationalClass) =
    new RationalClass(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )
  def * (that: RationalClass) =
    new RationalClass(numer * that.numer, denom * that.denom)

val oneHalf = new RationalClass(1, 2)
val twoThirds = new RationalClass(2, 3)
oneHalf + twoThirds

// ------------------------------------------------------------------------
// 3. Lazy Values
// ------------------------------------------------------------------------

var a = 1
object Demo:
  lazy val x = { println("initializing x"); a += 1; "done" }
  def y = { println("initializing y"); a += 1; "done" }
Demo
a
