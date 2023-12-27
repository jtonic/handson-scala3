object PackageOne:
  trait TwosComplement[N]:
    def equalsMinValue(n: N): Boolean
    def absOf(n: N): N
    def negationOf(n: N): N

    extension (n: N)
      def absOption: Option[N] = if equalsMinValue(n) then None else Some(absOf(n))
      def negateOption: Option[N] = if equalsMinValue(n) then None else Some(negationOf(n))

import PackageOne.TwosComplement

given tcOfByte: TwosComplement[Byte] with
  def equalsMinValue(n: Byte) = n == Byte.MinValue
  def absOf(n: Byte) = n.abs
  def negationOf(n: Byte) = (n).
  toByte
given tcOfShort: TwosComplement[Short] with
  def equalsMinValue(n: Short) = n == Short.MinValue
  def absOf(n: Short) = n.abs
  def negationOf(n: Short) = (n).
  toShort
given tcOfInt: TwosComplement[Int] with
  def equalsMinValue(n: Int) = n == Int.MinValue
  def absOf(n: Int) = n.abs
  def negationOf(n: Int) = n
given tcOfLong: TwosComplement[Long] with
  def equalsMinValue(n: Long) = n == Long.MinValue
  def absOf(n: Long) = n.abs
  def negationOf(n: Long) = n

10.absOption
10.negateOption
Int.MinValue.absOption
Int.MinValue.negateOption
