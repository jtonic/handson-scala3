import scala.util.control._

def processNumbers(): Option[(Int, Int)] = {
  var result: Option[(Int, Int)] = None
  val outer = new Breaks
  val inner = new Breaks
  outer.breakable:
    for (i <- 1 to 10) do
      inner.breakable:
        for (j <- 1 to 5) do
          println(s"[$i,$j]")
          if j == 3 then inner.break
          if i == 7 then
            result = Some((i, j))
            outer.break
  result
}

val r = processNumbers()
r
