import scala.compiletime.uninitialized

class Thermometer:
  var celsius: Float = uninitialized
  def fahrenheit = celsius * 9/5 + 32
  def fahrenheit_=(f: Float) =
    (celsius - 32) * 5/9
  override def toString = s"${fahrenheit}F/${celsius}C"


val t = Thermometer()
t.celsius = 10
t.toString
