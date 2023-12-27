import scala.collection.mutable.LinkedHashMap
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
val sol1 = solution(content("input"))
println(s"sol1 = $sol1")

object Part2:

  val digitsMap = LinkedHashMap(
    "one" -> 1,
    "two" -> 2,
    "three" -> 3,
    "four" -> 4,
    "five" -> 5,
    "six" -> 6,
    "seven" -> 7,
    "eight" -> 8,
    "nine" -> 9
  )

  def solution(input: String): Int =

    def replaceWithDigits(line: String) =
      digitsMap.foldLeft(line) { case (acc, (word, digit)) =>
        println(s"acc = $acc, word = $word, digit = $digit")
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
      .tapEach(println)
      .toList
      .sum

import Part2.solution as solution2
val sol2 = solution2(content("p2e.txt"))
println(s"sol2 = $sol2")
