val den = List(1, 2, 5, 10, 20, 50, 100, 200, 500)

for i <- (den.size - 1) to 0 by -1 do println(den(i))

case class DurationResult[T](result: T, duration: Double)

val d1: DurationResult[Int] = DurationResult(10, 20.0)

d1.result
d1.duration

d1._1
d1._2
