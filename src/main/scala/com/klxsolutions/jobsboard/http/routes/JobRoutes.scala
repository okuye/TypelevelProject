package com.klxsolutions.jobsboard.http.routes

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
import com.klxsolutions.jobsboard.domain.job.*
import com.klxsolutions.jobsboard.http.responses.*
import pureconfig.error.FailureReason

class JobRoutes[F[_]: Concurrent: Logger] private extends Http4sDsl[F] {

  private val database = mutable.Map[UUID, Job]()

  private val allJobsRoute: HttpRoutes[F] = HttpRoutes.of[F] { case POST -> Root =>
    Ok(database.values)
  }

  private val findJobRoute: HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root / UUIDVar(id) =>
    database.get(id) match {
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
  private val createJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "create" =>
      for {
        // _       <- Logger[F].info("Trying to add job")
        jobInfo <- req.as[JobInfo].logError(e => s"Parsing payload failed: $e")
        // _       <- Logger[F].info(s"Parsed job info $jobInfo")
        job     <- createJob(jobInfo)
        // _       <- Logger[F].info(s"Created job : $job")
        _       <- database.put(job.id, job).pure[F]
        resp    <- Created(job.id)
      } yield resp
  }

<<<<<<< HEAD
  // PUT /jobs/uuid { jobInfo }
=======
  // PUT /jobs/uuid { jobInfo } /
>>>>>>> origin/main
  private val updateJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ PUT -> Root / UUIDVar(id) =>
      database.get(id) match {
        case Some(job) =>
          for {
            jobInfo <- req.as[JobInfo]
            _       <- database.put(id, job.copy(jobInfo = jobInfo)).pure[F]
            resp    <- Ok()
          } yield resp
        case None => NotFound(FailureResponse(s"Cannot update job $id: not found"))
      }
  }

  private val deleteJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ DELETE -> Root / UUIDVar(id) =>
      database.get(id) match {
        case Some(job) =>
          for {
            _    <- database.remove(id).pure[F]
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
  def apply[F[_]: Concurrent: Logger] = new JobRoutes[F]
}
