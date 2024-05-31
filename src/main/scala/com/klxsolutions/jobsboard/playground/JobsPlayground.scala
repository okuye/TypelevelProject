package com.klxsolutions.jobsboard.playground

import cats.effect.*
import doobie.*
import doobie.implicits.*
import doobie.util.*
import doobie.hikari.HikariTransactor
import com.klxsolutions.jobsboard.core.*
import com.klxsolutions.jobsboard.domain.job.*
import scala.io.StdIn

object JobsPlayground extends IOApp.Simple {

  val postgresResource: Resource[IO, HikariTransactor[IO]] = for {
    ec <- ExecutionContexts.fixedThreadPool(32)
    xa <- HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver",
      "jdbc:postgresql:board",
      "docker",
      "docker",
      ec
    )

  } yield xa

  val jobInfo = JobInfo.minimal(
    company = "klx solutions ltd",
    title = "Software Engineering",
    description = "Greates Job",
    externalUrl = "klxsolutions.co.uk",
    remote = true,
    location = "Anywhere"
  )

  override def run: IO[Unit] = postgresResource.use { xa =>
    for {
      jobs <- LiveJobs[IO](xa)
      _ <- IO(println("Ready..Next...")) *> IO(StdIn.readLine)
      id <- jobs.create("olakunle@klxsolutions.co.uk", jobInfo)
      list <- jobs.all()
      _ <- IO(println(s"All jobs: $list. Next...")) *> IO(StdIn.readLine)
      _ <- jobs.update(id, jobInfo.copy(title = "Software Rockstar"))
      newJob <- jobs.find(id)
      _ <- IO(println(s"New job : $newJob.. Ready Next... $list.. Next...")) *> IO(StdIn.readLine)
      _ <- jobs.delete(id)
      listAfter <- jobs.all()
      _ <- IO(println(s"Deleted job. List now: $listAfter..Next...")) *> IO(StdIn.readLine)

    } yield ()
  }

}
