import scala.annotation.targetName
object ValueClasses:

  // BarCode opaque type
  opaque type BarCodeOT = String

  object BarCodeOT:
    def apply(code: String): BarCodeOT = code

    extension (code: BarCodeOT)
      // @targetName("valueBarCode")
      def value: String = code

  // Description opaque type
  opaque type DescriptionOT = String

  object DescriptionOT:
    def apply(description: String): DescriptionOT = description

    extension (description: DescriptionOT)
      def value: String = description

  // Quantity opaque type
  opaque type QuantityOT = Int

  object QuantityOT:
    def apply(quantity: Int): QuantityOT = quantity

    extension (quantity: QuantityOT)
      def value: Int = quantity

  // UnitsOfMeasure opaque types
  opaque type Centimeters = Double

  object Centimeters:
    def apply(cm: Double): Centimeters = cm

    extension (cm: Centimeters)
      def value: Double = cm
      def toInches: Inches = cm.value / 2.54

  opaque type Inches = Double

  object Inches:
    def apply(inches: Double): Inches = inches

    extension (inches: Inches)
      def value: Double = inches
      def toCentimeters: Centimeters = inches.value * 2.54

import ValueClasses.*

case class Product(code: BarCodeOT, description: DescriptionOT, quantity: QuantityOT, unitOfMeasure: Centimeters)


trait BackEnd:
  def getByCode(code: BarCodeOT): Option[Product]
  def getByDescription(description: DescriptionOT): List[Product]

final class BackEndImpl extends BackEnd:
  override def getByCode(code: BarCodeOT): Option[Product] =
    Some(Product(code, DescriptionOT("*"), QuantityOT(1), Centimeters(1.0)))
  override def getByDescription(description: DescriptionOT): List[Product] =
    List(Product(BarCodeOT("*"), description, QuantityOT(1), Inches(1.0).toCentimeters))

val be1: BackEnd = BackEndImpl()

val aCode: BarCodeOT = BarCodeOT("1-234-234")
val aDescription: DescriptionOT = DescriptionOT("1234")
val aQuantity: QuantityOT = QuantityOT(2)
val anDecimeter: Centimeters = Centimeters(10.0)
val tenInches: Inches = Inches(10.0)

be1.getByCode(aCode)
be1.getByDescription(aDescription)

aCode.value
aDescription.value
aQuantity.value
anDecimeter.value
anDecimeter.toInches.value
tenInches.toCentimeters.value

aCode.value.charAt(0)
// aCode.charAt(0) // error: value charAt is not a member of ValueClasses.BarCodeOT
