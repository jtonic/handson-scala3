import ro.jtonic.handson.scala3.conference._

val ticket1 = Ticket(TicketId("123"), TicketType.Paid, BigDecimal(50.0))

val ticket2 = Ticket(TicketId("222"), TicketType.Paid, BigDecimal(50.0)) // This will not compile

val ticketId = ticket1.id match { case TicketId(id) => println(s"Ticket id is $id") }

ticket1.id.asOption.getOrElse("0")

val ticketId3 = TicketId(null)
ticketId3.asOption.getOrElse("0")

val value1: Int = 10
val value2: Null | Int = null

Option(value1).getOrElse(0)
Option(value2).getOrElse(0)

val me = "Tony"

me.length()
