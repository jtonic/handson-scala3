package ro.jtonic.handson.scala3

import scala.collection._
import org.scalatest.Assertions
import org.junit.Test
import org.scalatestplus.junit.JUnitRunner
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.diagrams.Diagrams

/*
Here's an example of a FunSuite with Matchers mixed in:
 */
@RunWith(classOf[JUnitRunner])
class MySuite extends AnyFunSuite with Matchers with Diagrams {

  test("An empty list should be empty") {
    List() shouldBe empty
    Nil shouldBe empty
  }

  test("A non-empty list should not be empty") {
    List.empty[Int] shouldBe empty
    // assert(List.empty[Int].nonEmpty) // useful when using diagrams
    List("fee", "fie", "foe", "fum") should not be empty
  }

  test("A list's length should equal the number of elements it contains") {
    List() should have length (0)
    List(1, 2) should have length (2)
    List("fee", "fie", "foe", "fum") should have length (4)
  }
}

class ExampleSpec extends AnyFunSpec with Matchers {

  describe("An ArrayStack") {

    it("should pop values in last-in-first-out order") {
      val stack = new mutable.ArrayStack[Int]
      stack.push(1)
      stack.push(2)
      assert(stack.pop() === 2)
      assert(stack.pop() === 1)
    }

    it("should throw RuntimeException if an empty array stack is popped") {
      val emptyStack = new mutable.ArrayStack[Int]
      intercept[RuntimeException] {
        emptyStack.pop()
      }
    }
  }
}
