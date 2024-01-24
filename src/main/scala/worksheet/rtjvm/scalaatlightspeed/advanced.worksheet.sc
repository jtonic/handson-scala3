import scala.util.Failure
import scala.util.Success
import scala.util.Try

// ------------------------------------------------------------
// 1. Option
// ------------------------------------------------------------

def canReturnNullString(a: Int): String = a match
  case 1 => "A string one"
  case _ => null

canReturnNullString(1)
canReturnNullString(10)

val opt1 = Option(canReturnNullString(10))
val opt2 = Option(canReturnNullString(1))

val result = opt1 match
  case Some(value) => s"Value: $value"
  case None => ""
result

// ------------------------------------------------------------
// 2. Try
// ------------------------------------------------------------

def aMethodWhichThrowsAnException() = throw Exception("Boom!!!")

Try {
  aMethodWhichThrowsAnException()
} match
  case Success(value) => s"Successfully execute the method. Value: $value"
  case Failure(ex) => s"Failed with message: ${ex.getMessage()}"

// ------------------------------------------------------------
// 3. Implicits
// ------------------------------------------------------------

implicit val aNumber: Int = 10
def useImplicit(implicit aNumber: Int) = aNumber + 1
useImplicit

// ------------------------------------------------------------
// 4. Implicit conversions
// ------------------------------------------------------------

implicit class MyRichInteger(n: Int):
  def isEven(): Boolean = n % 2 == 0

10.isEven()
