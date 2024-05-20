import ro.jtonic.handson.scala3.util.array.{toArray, print}

val arr1 =
  """
  |x00
  |0x0
  |00x
  |""".toArray()

arr1.print()

val arr2 =
  """
  |x,0,0
  |0,x,0
  |0,0,x
  |""".toArray(',')

arr2.print()
