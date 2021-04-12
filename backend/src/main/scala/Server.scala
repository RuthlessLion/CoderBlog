package ru.codercat

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import services.PostsService
import ru.codercat.utils.HttpError

import org.http4s.Response

object Server extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- BlazeServerBuilder[IO]
      .bindHttp(8080, host = "0.0.0.0")
      .withHttpApp(PostsService())
      .withServiceErrorHandler( _ => {
          case HttpError(code) => IO.delay(Response[IO](status = code))
        }
      )
      .resource
      .use(_ => IO.never)
  } yield ExitCode.Success
}
