package ro.jtonic.handson.scala3.algorithms

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.util.control.Breaks._
import scala.util.control.Breaks


class GuessPwdSuite extends AnyFunSuite with Matchers:

  test("Guess pwd - brute force") {

    def guessPassword(secretPassword: String): Option[String] = {
      val chars = ('a' to 'z') ++ ('0' to '9')
      val charsCount = chars.length
      var builder = new StringBuilder(secretPassword.length)
      var isCompleted = false
      var guessedPassword: Option[String] = None

      val innerLoop = new Breaks()
      val outerLoop = new Breaks()

      outerLoop.breakable {
        for (length <- 2 to 8) {
          val indices = Array.fill(length)(0)
          var isCompleted = false
          val builder = new StringBuilder()
          var count = 0L
          while (!isCompleted) {
            builder.clear()
            for (i <- 0 until length) {
              builder += chars(indices(i))
            }
            val guess = builder.toString()
            println(s"[$count] Guessing $guess")
            if (guess == secretPassword) {
              println(s"Password Guessed! '$guess'")
              guessedPassword = Some(guess)
              outerLoop.break
            }
            count += 1
            if (count % 10000000 == 0) {
              println(s" > Checked: $count.");
            }
            indices(length - 1) = indices(length - 1) + 1;
            if (indices(length - 1) >= charsCount) {
              breakable {
                for (i <- length - 1 to 0 by -1) {
                  indices(i) = 0
                  indices(i - 1) = indices(i - 1) + 1
                  if (indices(i - 1) < charsCount) { break; }
                  if (i - 1 == 0 && indices(0) >= charsCount) {
                    isCompleted = true;
                    break;
                  }
                }
              }
            }
          }
        }
      }
      guessedPassword
    }
    guessPassword("bcd") shouldBe Some("bcd")
  }
