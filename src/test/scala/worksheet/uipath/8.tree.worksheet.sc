import scala.collection.mutable.ArrayBuffer
import org.scalatest.matchers.should.Matchers.*
import scala.util.control.Breaks.*

object Model

  trait Tree[T](val value: T):
    var parent: Node[T] = null
    override def toString() =
      s"value: $value, parent: [$parent]"


  case class Leaf[T] private(_value: T) extends Tree[T](_value)

  case class Node[T] private (_value: T) extends Tree[T](_value):
    private val _children: ArrayBuffer[Tree[T]] = ArrayBuffer.empty
    def children: List[Tree[T]] = _children.toList
    def addChild(child: Tree[T]): Unit =
      child.parent = this
      _children += child
    override def toString(): String =
      s"""
      ${super.toString()}, children: ${this._children.size}
      """.stripLeading()

  object Leaf:
    def apply[T](value: T): Tree[T] = new Leaf(value)

  object Node:
    def apply[T](value: T, children: Tree[T]*): Node[T] =
      val n: Node[T] = new Node(value)
      for c <- children do n.addChild(c)
      n

object Algebra
  import Model.*

  def getCommonParent[T](a: Tree[T], b: Tree[T]): Tree[T] =
    val ad = a.depth()
    val bd = b.depth()
    var an: Tree[T] = a
    var bn: Tree[T] = b
    if ad < bd then bn = b.moveUp(bd - ad)
    else if ad > bd then an = a.moveUp(ad - bd)
    else ()

    println(s"[before] an: ${an.value}, bn: ${bn.value}")
    var result: Option[Tree[T]] = None
    breakable:
      while true do
        if an.parent == bn.parent then
          result = Some(an.parent)
          break()
        else
          println(s"[loop] an: ${an.parent.value}, bn: ${bn.parent.value}")
          an = an.parent
          bn = bn.parent
    result.get

  extension [T](self: Tree[T])
    def traverse() =
      ???

    def depth(): Int =
      var depth: Int = 0
      var current = self
      while (current.parent != null)
        depth += 1
        current = current.parent
      depth

    def moveUp(levels: Int): Tree[T] =
      var l = levels
      var target: Tree[T] = self
      while (l > 0) do
        target = target.parent
        l -= 1
      target

object Data
  import Model.*, Algebra.*

  val tree = Node(10,
    Node(3,
      Leaf(2)
    ),
    Node(7,
      Leaf(5),
      Leaf(8),
      Leaf(9),
    ),
    Node(13,
      Node(20,
        Leaf(35),
        Node(33,
          Node(18),
          Node(20)
        )
      )
    ),
    Node(100,
      Leaf(17)
    ),
  )

// kkmk

val leaf2 = tree.children(0).asInstanceOf[Node[Int]].children(0)
leaf2.value
leaf2.parent.value shouldBe 3
leaf2.parent.parent.value shouldBe 10
leaf2.depth() shouldBe 2
leaf2.moveUp(2).value shouldBe 10

val leaf35 = tree.children(2).asInstanceOf[Node[Int]].children(0).asInstanceOf[Node[Int]].children(0)
leaf35.value shouldBe 35
leaf35.parent.value shouldBe 20

val leaf18 = tree.children(2).asInstanceOf[Node[Int]].children(0).asInstanceOf[Node[Int]].children(1).asInstanceOf[Node[Int]].children(0)

leaf18.value shouldBe 18

val leaf5 = tree.children(1).asInstanceOf[Node[Int]].children(0)
leaf5.value shouldBe 5
val leaf9 = tree.children(1).asInstanceOf[Node[Int]].children(2)
leaf9.value shouldBe 9

getCommonParent(leaf2, leaf35).value shouldBe 10
getCommonParent(leaf18, leaf35).value shouldBe 20
getCommonParent(leaf5, leaf9).value shouldBe 7
