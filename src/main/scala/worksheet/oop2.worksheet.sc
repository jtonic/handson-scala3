abstract class Element:
  import Element.elem

  def contents: Vector[String]
  def height: Int = contents.length
  def width: Int = if height == 0 then 0 else contents(0).length

  def above(that: Element): Element =
    elem(this.contents ++ that.contents)

  def beside(that: Element): Element =
    elem(
      for (line1, line2) <- this.contents zip that.contents
      yield line1 + line2
    )

  override def toString = contents.mkString("\n")

object Element:

  private class VectorElement (val contents: Vector[String]) extends Element

  private class LineElement(s: String) extends VectorElement(Vector(s)):
    override def width = s.length
    override def height = 1

  private class UniformElement(
    ch: Char,
    override val width: Int,
    override val height: Int
  ) extends Element:
    private val line = ch.toString * width
    def contents = Vector.fill(height)(line)

  def elem(contents: Vector[String]): Element =
    VectorElement(contents)

  def elem(chr: Char, width: Int, height: Int): Element =
    UniformElement(chr, width, height)

  def elem(line: String): Element =
    LineElement(line)

val elemsOne: Element = Element.elem(Vector("hello", "world"))
elemsOne.height
elemsOne.width


val elemsTwo: Element = Element.elem("hello")
elemsTwo.height
elemsTwo.width


import Element.*
val e1 = elem(Vector("hello", "world"))
val e2 = elem("hello")
val e3 = elem('x', 2, 3)
