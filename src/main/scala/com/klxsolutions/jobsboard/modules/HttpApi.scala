package com.klxsolutions.jobsboard.http.modules

import org.http4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.*
import cats.effect.*
import cats.implicits.*
import org.typelevel.log4cats.Logger
import com.klxsolutions.jobsboard.http.routes.*
import com.klxsolutions.jobsboard.modules.Core

class HttpApi[F[_]: Concurrent: Logger] private (core: Core[F]) {

  private val healthRoutes = HealthRoutes[F].routes
  private val jobRoutes = JobRoutes[F](core.jobs).routes

  val endpoints = Router {
    "/api" -> (healthRoutes <+> jobRoutes)
  }
}

object HttpApi {
  def apply[F[_]: Concurrent: Logger](core: Core[F]): Resource[F, HttpApi[F]] = 
    Resource.pure(new HttpApi[F](core))
}
