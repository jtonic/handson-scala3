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
