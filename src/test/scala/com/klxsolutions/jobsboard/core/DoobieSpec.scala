package com.klxsolutions.jobsboard.core

import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.util.ExecutionContexts
import doobie.hikari.HikariTransactor
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

trait DoobieSpec {

  val initScript: String
  val postgres: Resource[IO, PostgreSQLContainer[Nothing]] = {
    val acquire = IO {
      val container: PostgreSQLContainer[Nothing] = new PostgreSQLContainer("postgres")
      container.withInitScript(initScript)
      container.start()
      container.asInstanceOf[PostgreSQLContainer[Nothing]]
    }
    val release = (container: PostgreSQLContainer[Nothing]) => IO(container.stop())
    Resource.make(acquire)(release)
  }

  val transactor: Resource[IO, HikariTransactor[IO]] = for {
    db <- postgres
    ce <- ExecutionContexts.fixedThreadPool(1)
    xa <- HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver",
      db.getJdbcUrl(),
      db.getUsername(),
      db.getPassword(),
      ce
    )
  } yield xa
}
