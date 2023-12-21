import ro.jtonic.handson.scala3.conference.{Description, BarCode}

case class Product(code: BarCode, description: Description)

trait BackEnd{
  def getByCode(code: BarCode): Option[Product]
  def getByDescription(description: Description): List[Product]
}

val aCode = "1-234-234"
val aDescription = "ASUS motherboard"

class BackEndImpl extends BackEnd {
  override def getByCode(code: BarCode): Option[Product] = Some(Product(code, Description("*")))
  override def getByDescription(description: Description): List[Product] = List(Product(BarCode("*"), description))
}

val be1: BackEnd = BackEndImpl()

be1.getByCode(BarCode(aCode))
be1.getByDescription(Description(aCode))
