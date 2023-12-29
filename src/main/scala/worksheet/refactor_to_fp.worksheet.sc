// ------------------------------------------------------------------------
// imperative style
// ------------------------------------------------------------------------

def printMultiTable() =
  var i = 1
  // only i in scope here
  while i <= 10 do
    var j = 1
    // both i and j in scope here
    while j <= 10 do
      val prod = (i * j).toString
      // i, j, and prod in scope here
      var k = prod.length
      // i, j, prod, and k in scope here
        while k < 4 do
        print(" ")
        k += 1
      print(prod)
      j += 1
      // i and j still in scope; prod and k out of scope
    println()
    i += 1
  // i still in scope; j, prod, and k out of scope

printMultiTable()

// ------------------------------------------------------------------------
// functional style
// ------------------------------------------------------------------------
def multilineTable() =
  val tableSeq = // a sequence of row strings
    for row <- 1 to 10
    yield
      makeRow(row)
  tableSeq.mkString("\n")

def makeRow(row: Int): String = makeRowSeq(row).mkString

def makeRowSeq(row: Int): Seq[String] =
  for col <- 1 to 10
  yield
    val prod = (row * col).toString
    val padding = " " * (4 - prod.length)
    padding + prod

println(multilineTable())

val prod = (10 * 10).toString
val pad = " " * (4 - prod.length)
val paddedProd = s"'$pad$prod'"
