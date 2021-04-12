package ru.codercat

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder
import services.PostsService

object Server extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- BlazeServerBuilder[IO]
      .bindHttp(8080)
      .withHttpApp(PostsService())
      .resource
      .use(_ => IO.never)
  } yield ExitCode.Success
}
