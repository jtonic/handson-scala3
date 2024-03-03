//> using target.scope test
//> using scala 3.3.3
//> using dep "org.scalameta::munit::0.7.29"
//> using dep "org.scalatest::scalatest-shouldmatchers::3.2.18"

import org.scalatest.matchers.should.Matchers

class TestSuite extends munit.FunSuite with Matchers:

  test("equality test"):
    val obtained = "hello"
    val expected = "hello"
    obtained should equal (expected)
