//> using jvm 21
//> using scala 3.5.1
//> using dep "dev.zio::zio-http::3.0.0"
import zio._
import zio.http._

// to do
// 1. basic LSP shortcuts
// 2. routes from other classes/objects
// 3. routes to delegate to services 
// 4. serices to run in paralle or async

object ZioApp extends ZIOAppDefault:

  val homeRoot =
    Method.GET / Root -> handler(Response.text("Hello from Zio http"))

  // Responds with JSON
  val jsonRoute =
    Method.GET / "json" -> handler(Response.json("""{"greetings": "Hello World!"}"""))

  val app = Routes(homeRoot, jsonRoute)

  def run = Server.serve(app).provide(Server.default)


