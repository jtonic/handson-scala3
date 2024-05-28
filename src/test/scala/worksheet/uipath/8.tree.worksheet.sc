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

  def traverse[T](n: Tree[T]): Array[Tree[T]] =

    var current: Tree[T] = n
    val visitedNodes = ArrayBuffer.empty[Tree[T]]

    def visited(node: Tree[T], visitedNs: ArrayBuffer[Tree[T]]) = visitedNs.contains(node)
    def firstNotVisitedChild(node: Tree[T], visitedNs: ArrayBuffer[Tree[T]]): Tree[T] =
      node.children.find(n => !visitedNs.contains(n)).getOrElse(null)

    breakable:
      while true do
        if current.isInstanceOf[Leaf[T]] then
          visitedNodes += current
          current = current.parent // loop here
        else
          val crtNode: Node[T] = current.asInstanceOf[Node[T]]
          val visitedNode = visited(crtNode, visitedNodes)
          if !visitedNode then
            visitedNodes += crtNode
            current = crtNode.children(0) // loop here
          else
            val fnvChild: Tree[T] = firstNotVisitedChild(crtNode, visitedNodes)
            if fnvChild != null then
              current = fnvChild // loop here
            else if crtNode.parent != null then
              current = crtNode.parent // loop here
            else
              break()
    visitedNodes.toArray

  extension [T](self: Tree[T])

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

object Conversions
  given treeToNodeConversion[T]: Conversion[Tree[T], Node[T]] with
    def apply(x: Tree[T]): Node[T] = x.asInstanceOf[Node[T]]

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
          Leaf(18),
          Leaf(21)
        )
      )
    ),
    Node(100,
      Leaf(17)
    ),
  )

val leaf35 = tree.children(2).asInstanceOf[Node[Int]].children(0).asInstanceOf[Node[Int]].children(0)
val leaf18 = tree.children(2).children(0).children(1).children(0)
val leaf2 = tree.children(0).children(0)
val leaf5 = tree.children(1).children(0)
val leaf9 = tree.children(1).children(2)
val leaf21 = tree.children(2).children(0).children(1).children(1).value


getCommonParent(leaf2, leaf35).value shouldBe 10
getCommonParent(leaf18, leaf35).value shouldBe 20
getCommonParent(leaf5, leaf9).value shouldBe 7

leaf2.depth() shouldBe 2
leaf2.moveUp(2).value shouldBe 10

val traversed: Array[Tree[Int]] = traverse(tree)
traversed.size shouldBe 15
traversed.map(_.value).toList shouldBe List(10, 3, 2, 7, 5, 8, 9, 13, 20, 35, 33, 18, 21, 100, 17)
