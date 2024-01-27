package ro.jtonic.handson.scala3.dsl

import scala.concurrent.duration.*

// ----------------------------------------
// A small but interesting DSL example
// ----------------------------------------

package DurationUtils:
  sealed trait DSLKeyWord
  object sleep extends DSLKeyWord:
    def current(th: thread.type): thread.type = thread
  object thread extends DSLKeyWord
  extension (t: thread.type)
    def `for`(d: Duration): Unit = Thread.sleep(d.toMillis)

@main def main(): Unit = {
  import DurationUtils.*
  sleep current thread `for` 1.second
  println("Finish!!!")
}
