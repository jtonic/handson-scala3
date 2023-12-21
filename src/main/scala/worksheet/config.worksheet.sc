import org.scalacheck.Gen
import com.typesafe.config.{ConfigFactory, ConfigMemorySize}
import io.circe.generic.auto._
import io.circe.config.syntax._
import io.circe.config.parser
import scala.concurrent.duration.FiniteDuration

case class ServerMetadata(name: String, value: String, description: Option[String], valueType: String)
case class ServerSettings(
  hostName: String, port: Int, tls: Option[Boolean],
  aliases: List[String], info: Map[String, String], metadata: List[ServerMetadata])
case class AppConfig(serverSettings: ServerSettings)

val appConfig = parser.decode[AppConfig]()

appConfig match {
  case Left(err) => println(s"Error: $err")
  case Right(settings) => println(s"Settings: $settings")
}

val config = ConfigFactory.load()
val serverSettings = config.as[ServerSettings]("serverSettings")
serverSettings match
  case Left(err) => println(s"Error: $err")
  case Right(value) => println(s"Server settings: $value")

val serverSettings2 = parser.decodePath[ServerSettings]("serverSettings")

// ----------------------------------------
// with cats-effect
// ----------------------------------------
import cats.effect.IO
import cats.effect.unsafe.implicits.global
val serverSettings3 = parser.decodePathF[IO, ServerSettings](path = "serverSettings")
serverSettings3.unsafeRunSync()


// ----------------------------------------
// lenses
// ----------------------------------------

import monocle.Lens
import monocle.macros._

case class User(name: String, address: Address)
case class Address(streetNumber: Int, streetName: String)

val tonyAddress = Address(1, "Main St.")
val tony = User("Tony", tonyAddress)

// val tonyMoved = tony.focus(_.address.streetName).replace("Main St.")


val streetNameLens = GenLens[Address](_.streetName)
streetNameLens.get(tonyAddress)
val streetNumberLens = GenLens[Address](_.streetNumber)
streetNumberLens.get(tonyAddress)

val newAddress = streetNameLens.replace("Second St.")(tonyAddress)
streetNumberLens.modify(_ + 2)(newAddress)
tonyAddress
newAddress

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits._

def updateNumber(n: Int): Future[Int] = Future.successful(n + 1)

streetNumberLens.modifyF(updateNumber)(tonyAddress)

// ----------------------------------------
// compose lenses
// ----------------------------------------
(
    GenLens[Address](_.streetName).replace("Third St.") andThen
    GenLens[Address](_.streetNumber).modify(_ + 10)
)(tonyAddress)

GenLens[User](_.address).andThen(GenLens[Address](_.streetName)).replace("Fourth St.")(tony)

GenLens[User](_.address.streetName).replace("Fifth St.")(tony)

import monocle.Focus

Focus[User](_.address).andThen(GenLens[Address](_.streetName)).replace("Sixth St.")(tony)

// ----------------------------------------
// lenses on university
// ----------------------------------------

case class Lecturer(firstName: String, lastName: String, salary: Int)
case class Department(budget: Int, lecturers: List[Lecturer])
case class University(name: String, departments: Map[String, Department])

val uni = University("oxford", Map(
  "Computer Science" -> Department(45, List(
    Lecturer("john"  , "doe", 10),
    Lecturer("robert", "johnson", 16)
  )),
  "History" -> Department(30, List(
    Lecturer("arnold", "stones", 20)
  ))
))

val departments = Focus[University](_.departments)

departments.at("History").replace(None)(uni)

val physics = Department(36, List(
  Lecturer("daniel", "jones", 12),
  Lecturer("roger" , "smith", 14)
))

departments.at("Physics").replace(Some(physics))(uni)
