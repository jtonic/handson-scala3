import java.nio.file.Paths
import scala.io.Source
import java.nio.file.Files

object Part1:
  def solution(input: String): Int =

    def firstAndLastDigits(st: String) =
      val firstDigit = st.find(_.isDigit).get
      val lastDigit = st.reverse.find(_.isDigit).get
      s"$firstDigit$lastDigit".toInt

    input
      .linesIterator
      .map(firstAndLastDigits)
      .toList
      .sum

def content(fileName: String): String =
  val filePath = s"src/main/scala/worksheet/aoc2023/$fileName"
  Source.fromFile(filePath).mkString

import Part1.*
solution(content("input"))

object Part2:

  val digitsMap = Map(
    "one" -> 1,
    "two" -> 2,
    "three" -> 3,
    "four" -> 4,
    "five" -> 5,
    "six" -> 6,
    "seven" -> 7,
    "eight" -> 8,
    "nin" -> 9
  )

  def solution(input: String): Int =

    def replaceWithDigits(line: String) =
      digitsMap.foldLeft(line) { case (acc, (word, digit)) =>
        acc.replaceAll(word, digit.toString)
      }

    def firstAndLastDigits(line: String) =
      val firstDigit = line.find(_.isDigit).get
      val lastDigit = line.reverse.find(_.isDigit).get
      s"$firstDigit$lastDigit".toInt

    input
      .linesIterator
      .tapEach(println)
      .map(replaceWithDigits)
      .tapEach(println)
      .map(firstAndLastDigits)
      .toList
      .sum

import Part2.solution as s2
s2(content("p2e.txt"))
