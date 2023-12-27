val a = "  aaa  "
a.trim()

extension (str: String)
  def singleSpace = str.trim().replaceAll("\\s+", "").mkString(" ")

val a1 = "Tony is a Scala developer".singleSpace
val a2 = "   \tTony \t   is    a    Scala developer   ".singleSpace

a1 == a2

List(1, 2, 3).head
List(1, 2, 3).tail

List.empty[Int].headOption

extension [A](list: List[A])
  def headOption: Option[A] = list match
    case Nil => None
    case head :: _ => Some(head)
  def tailOption: Option[List[A]] = list match
    case Nil => None
    case _ :: tail => Some(tail)


List.empty[Int].headOption
List(1, 2, 3).headOption

List.empty[Int].tailOption
List(1, 2, 3).tailOption

extension (i: Int)
  private def isMinValue: Boolean = i == Int.MinValue
  def absOption: Option[Int] = if i.isMinValue then None else Some(i.abs)
  def negateOption: Option[Int] = if i.isMinValue then None else Some(-i)

Int.MinValue.absOption
Int.MinValue.negateOption
Int.MinValue.isMinValue

case class Amount(value: Int) {
  def unary_- : Amount = Amount(-value)
}
