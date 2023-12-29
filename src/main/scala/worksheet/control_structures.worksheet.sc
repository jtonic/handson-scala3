//-----------------------------------------------------
// for loop
//-----------------------------------------------------

val files = new java.io.File(".").listFiles.toList

for
  f <- files
  if f.isDirectory()
  fileName = f.getName()
  if !fileName.startsWith(".")
  if fileName != "target"
do
  println(fileName)

val directories =
  for
    f <- files
    if f.isDirectory()
    fileName = f.getName()
    if !fileName.startsWith(".")
    if fileName != "target"
  yield
    fileName

for i <- 1 to 10 do
  println(s"iteration $i")

//-----------------------------------------------------
// try catch
//-----------------------------------------------------
import java.net.*

def urlFor(path: String) =
  try
    new URL(path)
  catch
    case e: MalformedURLException =>
      new URL("http://www.scala-lang.org")

urlFor("Gigi Becali")
urlFor("https:/example.com")
urlFor(null)
