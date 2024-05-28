package com.klxsolutions.jobsboard.modules

import cats.effect.*
import cats.implicits.*
import doobie.hikari.HikariTransactor
import doobie.util.*
import cats.effect.kernel.Async
import cats.effect.kernel.Resource

import com.klxsolutions.jobsboard.core.*
import doobie.util.transactor.Transactor

final class Core[F[_]] private (val jobs: Jobs[F])

object Core {
  def postgresResource[F[_]: Async]: Resource[F, HikariTransactor[F]] = for {
    ec <- ExecutionContexts.fixedThreadPool(32)
    xa <- HikariTransactor.newHikariTransactor[F](
      "org.postgresql.Driver",
      "jdbc:postgresql:board",
      "docker",
      "docker",
      ec
    )

  } yield xa

  def apply[F[_]: Async](xa: Transactor[F]): Resource[F, Core[F]] =
    Resource
      .eval(LiveJobs[F](xa))
      .map(jobs => new Core(jobs))

}
