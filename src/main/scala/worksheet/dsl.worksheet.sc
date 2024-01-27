import scala.concurrent.duration.*

// ----------------------------------------
// A small but interesting DSL example
// ----------------------------------------

object DurationUtils: // <- this must be package but it doesn't work in worksheet
  sealed trait DSLKeyWord
  object sleeping extends DSLKeyWord:
    def current(th: thread.type): thread.type = thread
  object sleep extends DSLKeyWord:
    def current(th: thread.type): thread.type = thread
  object thread extends DSLKeyWord
  extension (t: thread.type)
    def `for`(d: Duration): Unit = Thread.sleep(d.toMillis)
    def during(d: Duration): Unit = Thread.sleep(d.toMillis)

import DurationUtils.*

sleeping current thread `for` 1.second
sleep current thread during 1.second

println("Finish!!!")
