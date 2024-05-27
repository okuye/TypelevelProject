package com.klxsolutions.jobsboard.http.routes

import com.klxsolutions.jobsboard.domain.job.*
import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.*
import cats.effect.*
import cats.implicits.*
import org.typelevel.log4cats.Logger

import scala.collection.mutable
import java.util.UUID

import com.klxsolutions.jobsboard.core.*
import com.klxsolutions.jobsboard.http.responses.*
import pureconfig.error.FailureReason

class JobRoutes[F[_]: Concurrent: Logger] private (jobs: Jobs[F]) extends Http4sDsl[F] {

  private val allJobsRoute: HttpRoutes[F] = HttpRoutes.of[F] { case POST -> Root =>
    for {
      jobList <- jobs.all()
      resp <- Ok(jobList)
    } yield resp
  }

  private val findJobRoute: HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root / UUIDVar(id) =>
    jobs.find(id).flatMap {
      case Some(job) => Ok(job)
      case None      => NotFound(FailureResponse(s"Job $id not found."))
    }
  }

  private def createJob(jobInfo: JobInfo): F[Job] =
    Job(
      id = UUID.randomUUID(),
      date = System.currentTimeMillis(),
      ownerEmail = "olastest@gmail.com",
      jobInfo = jobInfo,
      active = true
    ).pure[F]

  import com.klxsolutions.jobsboard.logging.syntax.*
  private val createJobRoute: HttpRoutes[F] = HttpRoutes.of[F] { case req @ POST -> Root / "create" =>
    for {
      jobInfo <- req.as[JobInfo].logError(e => s"Parsing payload failed: $e")
      jobId <- jobs.create("TODO@klxsolutions.com", jobInfo)
      resp <- Created(jobId)
    } yield resp
  }

  private val updateJobRoute: HttpRoutes[F] = HttpRoutes.of[F] { case req @ PUT -> Root / UUIDVar(id) =>
    for {
      jobInfo <- req.as[JobInfo]
      maybeNewJob <- jobs.update(id, jobInfo)
      resp <- maybeNewJob match {
        case Some(job) => Ok()
        case None      => NotFound(FailureResponse(s"Cannot update job $id: not found"))
      }
    } yield resp
  }

  private val deleteJobRoute: HttpRoutes[F] = HttpRoutes.of[F] { case req @ DELETE -> Root / UUIDVar(id) =>
    jobs.find(id).flatMap {
      case Some(job) =>
        for {
          _ <- jobs.delete(id)
          resp <- Ok()
        } yield resp
      case None => NotFound(FailureResponse(s"Cannot delete job $id: not found"))
    }
  }

  val routes = Router(
    "/jobs" -> (allJobsRoute <+> findJobRoute <+> createJobRoute <+> updateJobRoute <+> deleteJobRoute)
  )
}

object JobRoutes {
  def apply[F[_]: Concurrent: Logger](jobs: Jobs[F]) = new JobRoutes[F](jobs)
}
