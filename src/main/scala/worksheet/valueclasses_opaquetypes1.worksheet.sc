object NonEmptyStrings:
  opaque type NonEmptyString <: String = String
  object NonEmptyString:
    def apply(s: String): NonEmptyString =
      require(s.nonEmpty)
      s
    def from(s: String): Option[NonEmptyString] =
      if s.nonEmpty then Some(s) else None

    extension (s: NonEmptyString)
      def value: String = s

import NonEmptyStrings.*

val aName = NonEmptyString("Tony")
val anInvalidStr = ""
val anInvalidName = NonEmptyString.from(anInvalidStr)

val str: String = aName
str.charAt(0)
NonEmptyString("Irina").charAt(0)


object StreetAndCity:
  opaque type Street = String
  object Street:
    def apply(street: String): Street = street
  opaque type City = String
  object City:
    def apply(city: String): City = city

import StreetAndCity.*

val aStreet = Street("Main Street")
aStreet.isInstanceOf[String]
aStreet.isInstanceOf[Street]

City("New York") match
  case s: String => s.charAt(0)

// hacking the immutability of the IArray
val iarr = IArray(1, 2, 3)
iarr.toList
iarr match
  case arr: Array[Int] => arr(0) = 0
iarr.toList
