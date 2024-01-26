// ----------------------------------------
// 1.Promises
// ----------------------------------------
import scala.concurrent.Promise

val promise = Promise[Int]
val future = promise.future

promise.success(10)
future.value
