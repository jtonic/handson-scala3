package ro.jtonic.handson.scala3

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import ro.jtonic.handson.scala3.util.array.{toArray, print}

class TmpSuite extends AnyFunSuite with Matchers {

  test("test 1") {
    val arr2 =
      """
        |x00
        |0x0
        |00x
        |""".toArray()

    arr2.print()
  }
}
