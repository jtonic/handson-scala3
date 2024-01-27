import scala.concurrent.duration.*
//--------------------------------------------------
// 1 Singleton Types
//--------------------------------------------------

def processThree(in: 3): String = in.toString()

val three: 3 = 3
processThree(three)
processThree(3)
// processThree(4) // compile error: found: Int(4) required: 3

// processThree(4) // compile error: found: Int(4) required: 3

object ConcurrentUtil:
  sealed trait SupportedThread
  object CurrentThread extends SupportedThread
  object VirtualThread extends SupportedThread

  extension (d: Duration)
    // CurrentThread.type is a singleton type with only one inhabitant: the object itself
    // Useful so far in DSL
    def sleeping(th: CurrentThread.type) = Thread.sleep(d.toMillis)
    def sleeping(th: VirtualThread.type) = Thread.sleep(d.toMillis) // I suppose is Thread.virtualThread.sleep(d.toMillis)

import ConcurrentUtil.*

2.seconds sleeping CurrentThread
3.seconds sleeping VirtualThread


println("Finish")
