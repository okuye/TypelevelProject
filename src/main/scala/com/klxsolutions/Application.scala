package com.klxsolutions.jobsboard

import cats.effect.{IO, IOApp}
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.Logger
import com.klxsolutions.jobsboard.modules.Core
import com.klxsolutions.jobsboard.http.modules.HttpApi
import com.klxsolutions.jobsboard.config.*
import pureconfig.ConfigSource
import org.typelevel.log4cats.slf4j.Slf4jLogger
import com.klxsolutions.jobsboard.config.syntax.loadF

object Application extends IOApp.Simple {

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  override def run = ConfigSource.default.loadF[IO, EmberConfig].flatMap { config =>

    val appResource = for {
      core <- Core[IO]
      httpApi <- HttpApi[IO](core)
      server <- EmberServerBuilder
        .default[IO]
        .withHost(config.host)
        .withPort(config.port)
        .withHttpApp(httpApi.endpoints.orNotFound)
        .build
    } yield server

    appResource.use(_ => IO.println("klx solutions rock!") *> IO.never)
  }
}
