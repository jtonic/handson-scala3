import scala.annotation.targetName
object ValueClasses:

  // BarCode opaque type
  opaque type BarCodeOT = String

  object BarCodeOT:
    def apply(code: String): BarCodeOT = code

  extension (code: BarCodeOT)
    @targetName("valueBarCode")
    def value: String = code

  // Description opaque type
  opaque type DescriptionOT = String

  object DescriptionOT:
    def apply(description: String): DescriptionOT = description

  extension (description: DescriptionOT)
    @targetName("valueDescription")
    def value: String = description

  // Quantity opaque type
  opaque type QuantityOT = Int

  object QuantityOT:
    def apply(quantity: Int): QuantityOT = quantity

  extension (quantity: QuantityOT)
    @targetName("valueQuantity")
    def value: Int = quantity


import ValueClasses.*

case class Product(code: BarCodeOT, description: DescriptionOT, quantity: QuantityOT = QuantityOT(1))


trait BackEnd:
  def getByCode(code: BarCodeOT): Option[Product]
  def getByDescription(description: DescriptionOT): List[Product]

final class BackEndImpl extends BackEnd:
  override def getByCode(code: BarCodeOT): Option[Product] = Some(Product(code, DescriptionOT("*")))
  override def getByDescription(description: DescriptionOT): List[Product] = List(Product(BarCodeOT("*"), description))

val be1: BackEnd = BackEndImpl()

val aCode: BarCodeOT = BarCodeOT("1-234-234")
val aDescription: DescriptionOT = DescriptionOT("1234")
val aQuantity: QuantityOT = QuantityOT(2)

be1.getByCode(aCode)
be1.getByDescription(aDescription)

aCode.value
aDescription.value
aQuantity.value
