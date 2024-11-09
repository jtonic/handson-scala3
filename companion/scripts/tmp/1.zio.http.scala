//> using scala "3.5.1"
//> using dep "dev.zio::zio:2.1.11"
//> using dep "dev.zio::zio-http:3.0.1"
//> using jvm "21"

import zio._
import zio.http._

object GreetingServer extends ZIOAppDefault {
  val routes =
    Routes(
      Method.GET / Root -> handler(Response.text("Greetings at your service")),
      Method.GET / "greet" -> handler { (req: Request) =>
        val name = req.queryParamToOrElse("name", "World")
        Response.text(s"Hello $name!")
      }
    )

  def run = Server.serve(routes).provide(Server.default)
}
