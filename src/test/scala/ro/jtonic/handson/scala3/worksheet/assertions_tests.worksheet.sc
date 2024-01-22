assert(true, "This is a test")

def test(a: Boolean, msg: => String) = {
  if (!a) throw new AssertionError(msg)
}

def test2(a: Boolean, msg: String) = {
  if (!a) throw new AssertionError(msg)
}

test(1 == 1, { println("Boom!!!"); "1 == 1" })
test2(1 == 1, { println("Boom!!!"); "1 == 1" })

val result = "aaaa" ensuring ({ _.size >= 3 }, "aaaa is too short")
result
