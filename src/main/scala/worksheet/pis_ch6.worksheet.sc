class Rational(n: Int, d: Int):

  require(d != 0, "denominator should not be zero")
  private val numer: Int = n
  private val denom: Int = d

  def this(n: Int) = this(n, 1)

  override def toString = s"$numer/$denom"

  def >(other: Rational) =
    this.numer * other.denom > other.numer * this.denom

  def +(other: Rational): Rational =
    Rational(
      numer * other.denom + denom * other.numer,
      denom * other.denom
    )

  def +(other: Int): Rational =
    this + Rational(other)

extension (i: Int)
  def +(other: Rational): Rational =
    Rational(i) + other

val half = Rational(1, 2)
val toThirds = Rational(2, 3)

half
toThirds

half + toThirds
toThirds > half
