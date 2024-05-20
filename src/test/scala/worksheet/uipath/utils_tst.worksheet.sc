import ro.jtonic.handson.scala3.util.array.{toArray, print, load}

val arr11 =
  """
  |x00
  |0x0
  |00x
  |""".toArray()

arr11.print()

val arr12 =
  """
  |x,0,0
  |0,x,0
  |0,0,x
  |""".toArray(',')

arr12.print()


val arr21 = load("src/test/scala/worksheet/uipath/data1.txt")
arr21.print()

val arr22 = load("src/test/scala/worksheet/uipath/data2.txt", ',')
arr22.print()
