package ro.jtonic.handson.scala3.hollywood

package app:

  import cats.effect.IOApp

  object HollywoodApp extends IOApp:
    import org.http4s.server.blaze.BlazeServerBuilder
    import org.http4s.implicits._
    import cats.implicits._
    import cats.effect._
    import cats.syntax.all._
    import scala.concurrent.ExecutionContext.global
    import org.typelevel.log4cats.slf4j.loggerFactoryforSync
    import routes._

    def run(args: List[String]): IO[ExitCode] =
      BlazeServerBuilder[IO](global)
              .bindHttp(8080, "localhost")
              .withHttpApp(allRoutes[IO].orNotFound)
              .resource
              .use(_ => IO.never)
              .as(ExitCode.Success)
