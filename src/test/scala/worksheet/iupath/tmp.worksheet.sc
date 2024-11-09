import org.scalatest.matchers.should.Matchers.*

List(1, 2) should equal (List(1, 2))


var a = 1
var b = 1

val c = true

if c
then
  a += 1
  b += 1

a
b
