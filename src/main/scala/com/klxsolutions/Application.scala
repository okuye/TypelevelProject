package com.klxsolutions.jobsboard

import cats.effect.{IO, IOApp}
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.Logger
import com.klxsolutions.jobsboard.http.HttpApi
import com.klxsolutions.jobsboard.config.*
import com.klxsolutions.jobsboard.config.syntax.*
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderException
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Application extends IOApp.Simple {

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  override def run = ???

  // override def run = ConfigSource.default.loadF[IO, EmberConfig].flatMap { config =>
  //   EmberServerBuilder
  //     .default[IO]
  //     .withHost(config.host)
  //     .withPort(config.port)
  //     .withHttpApp(HttpApi[IO].endpoints.orNotFound)
  //     .build
  //     .use(_ => IO.println("Server ready!") *> IO.never)
  // }
}
