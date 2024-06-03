import org.scalatest.matchers.should.Matchers.*
import ro.jtonic.handson.scala3.util.function.*

def parseToInt(a: String): Int = a.toInt

def sum (a: Int, b: Int) = a + b

def inc (a: Int) = a + 1

// usages

parseToInt.andThen(inc)("2")
(parseToInt andThen inc)("2")

"3" |> (parseToInt >> inc)

"3" |> (parseToInt >> sum.curried(1)) shouldBe 4

"3" |> parseToInt |> sum.curried(1) shouldBe 4
