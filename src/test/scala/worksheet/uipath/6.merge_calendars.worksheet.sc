import org.scalatest.matchers.should.Matchers.*
import scala.util.control.Breaks.*

object Model:
  case class CalendarEntry(description: String, startTime: Int)

  object CalendarEntry:
    def empty = CalendarEntry(null, -1)
    def max(c1: CalendarEntry, c2: CalendarEntry) =
      if c1.startTime >= c2.startTime
      then c1
      else c2

object Algebra:
  import Model.*

  def merge(c1: Array[CalendarEntry], c2: Array[CalendarEntry]): Array[CalendarEntry] =

    val s1 = c1.size
    val s2 = c2.size
    val s = s1 + s2
    val r: Array[CalendarEntry] = Array.fill(s)(CalendarEntry.empty)
    var i = 0
    var i1 = 0
    var i2 = 0

    breakable:
      while
        true
      do
        // all s2 items are visited
        if i2 == s2
        then
          Array.copy(c1, i1, r, i, c1.size - i1)
          break()

        // all s1 items are visited
        if i1 == s1
        then
          Array.copy(c2, i2, r, i, c2.size - i2)
          break()

        // otherwise
        val e1 = c1(i1)
        val e2 = c2(i2)
        if e1.startTime < e2.startTime
        then
          r(i) = e1
          i1 += 1
        else
          r(i) = e2
          i2 += 1
        i += 1

    r

object Data:
  import Model.*

  val cal1e = Array.empty[CalendarEntry]
  val cal2e = Array.empty[CalendarEntry]

  val cal11 = Array(
    CalendarEntry("c1_8", 8),
    CalendarEntry("c1_9", 9),
    CalendarEntry("c1_12", 12),
    CalendarEntry("C1_15", 15),
    CalendarEntry("C1_18", 18),
    CalendarEntry("C1_19", 19),
    CalendarEntry("C1_20", 20),
  )

  val cal21 = Array(
    CalendarEntry("c2_10", 10),
    CalendarEntry("c2_11", 11),
    CalendarEntry("c2_17", 17),
    )

  val cal12 = Array(
    CalendarEntry("c1_8", 8),
    CalendarEntry("c1_9", 9),
    CalendarEntry("c1_12", 12),
    CalendarEntry("C1_15", 15),
    CalendarEntry("C1_18", 18),
    CalendarEntry("C1_19", 19),
    CalendarEntry("C1_20", 20),
  )

  val cal22 = Array(
    CalendarEntry("c2_7", 7),
    CalendarEntry("c2_21", 21),
    )

// Asserts
import Data.*, Model.*, Algebra.*

merge(cal1e, cal2e) should be (Array.empty[CalendarEntry])
merge(cal11, cal2e) should be (cal11)
merge(cal1e, cal21) should be (cal21)
merge(cal11, cal21) should be (
  Array(
    CalendarEntry("c1_8", 8),
    CalendarEntry("c1_9", 9),
    CalendarEntry("c2_10", 10),
    CalendarEntry("c2_11", 11),
    CalendarEntry("c1_12", 12),
    CalendarEntry("C1_15", 15),
    CalendarEntry("c2_17", 17),
    CalendarEntry("C1_18", 18),
    CalendarEntry("C1_19", 19),
    CalendarEntry("C1_20", 20),
  )
)

merge(cal12, cal22) should be (
  Array(
    CalendarEntry("c2_7", 7),
    CalendarEntry("c1_8", 8),
    CalendarEntry("c1_9", 9),
    CalendarEntry("c1_12", 12),
    CalendarEntry("C1_15", 15),
    CalendarEntry("C1_18", 18),
    CalendarEntry("C1_19", 19),
    CalendarEntry("C1_20", 20),
    CalendarEntry("c2_21", 21),
  )
)
