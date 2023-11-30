package ro.jtonic.handson.scala3.conference

import java.time.ZonedDateTime

enum Gender:
  case Male, Female

enum Role:
    case Speaker
    case Attendee

case class Person(id: String, name: String, gender: Gender, age: Int, role: Role = Role.Attendee)

case class Conference(id: String, name: String, people: List[Person])

case class Session(id: String, title: String, speakerId: String, time: ZonedDateTime)

case class Room(id: Int, name: String, capacity: Int)

enum TicketType:
    case Free
    case Paid
    case StudentDiscount

final case class TicketId(value: String) extends AnyVal:
  def asOption: Option[String] = Option(value)

case class Ticket(id: TicketId, ticketType: TicketType, price: BigDecimal)
