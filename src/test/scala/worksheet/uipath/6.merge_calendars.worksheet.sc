import java.util.Calendar
import scala.annotation.retains
import scala.collection.mutable.ArrayBuffer
import org.scalatest.matchers.should.Matchers.*
import scala.util.control.Breaks.*


case class CalendarEntry(description: String, startTime: Int)

object CalendarEntry:
  def empty = CalendarEntry(null, -1)
  def max(c1: CalendarEntry, c2: CalendarEntry) =
    if c1.startTime >= c2.startTime
    then c1
    else c2

val cal11 = Array(
  CalendarEntry("c1_8", 8),
  CalendarEntry("c1_9", 9),
  CalendarEntry("c1_12", 12),
  CalendarEntry("C1_15", 15),
  CalendarEntry("C1_18", 18),
  CalendarEntry("C1_19", 19),
  CalendarEntry("C1_20", 20),
)

def merge(c1: Array[CalendarEntry], c2: Array[CalendarEntry]): Array[CalendarEntry] =

  val s1 = c1.size
  val s2 = c2.size
  val s = s1 + s2
  val r: Array[CalendarEntry] = Array.fill(s)(CalendarEntry.empty)
  val i = 0
  val i1 = 0
  val i2 = 0

  breakable:
    while
      true
    do
      if i2 == s2
      then
        Array.copy(c1, i1, r, i, c1.size - i1)
        break()
      if i1 == s1
      then
        Array.copy(c2, i2, r, i, c2.size - i2)
        break()
  r


val cal21 = Array.empty[CalendarEntry]

val cal12 = Array.empty[CalendarEntry]
val cal22 = Array(
  CalendarEntry("c2_10", 10),
  CalendarEntry("c2_11", 11),
  CalendarEntry("c2_17", 17),
  )

merge(cal11, cal21) should be (cal11)
merge(cal21, cal22) should be (cal22)


// def merge(c1: Array[CalendarEntry], c2: Array[CalendarEntry]): List[CalendarEntry] =
//   val size = c1.size + c2.size
//   val r: ArrayBuffer[CalendarEntry] = ArrayBuffer.fill(size)(CalendarEntry.empty)

//   var i = 0
//   var p1 = 0
//   var p2 = 0

//   while
//     p1 < c1.size && p2 < c2.size
//   do
//     val e1 = c1(p1)
//     val e2 = c2(p2)
//     println(s"$e1 : $e2")
//     if e1.startTime < e2.startTime
//     then
//       r(i) = e1
//       p1 += 1
//     else
//       r(i) = e2
//       p2 += 1
//     i += 1

//   r.toList

/* val mergedCalendars = merge(cal1, cal2)

mergedCalendars

mergedCalendars should equal (
  List(
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
 */
