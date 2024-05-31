package com.klxsolutions.jobsboard.http.validation

import io.circe.generic.auto.*
import org.http4s.circe.CirceEntityCodec.*
import cats.*
import cats.implicits.*
import org.http4s.*
import org.http4s.dsl.*
import org.http4s.implicits.*
import org.typelevel.log4cats.Logger
import validators.*
import com.klxsolutions.jobsboard.logging.syntax.*
import cats.data.*
import cats.data.Validated.*
import com.klxsolutions.jobsboard.http.responses.FailureResponse

object syntax {

  def validateEntity[A](entity: A)(using validator: Validator[A]): ValidationResult[A] =
    validator.validate(entity)

  trait HttpValidationsDsl[F[_]: MonadThrow: Logger] extends Http4sDsl[F] {

    extension (req: Request[F])
      def validate[A: Validator](serverLogicIfValid: A => F[Response[F]])(using EntityDecoder[F, A]): F[Response[F]] =
        req
          .as[A]
          .logError(e => s"Parsing payload failed: $e")
          .map(validateEntity)
          .flatMap {
            case Valid(entity)   => serverLogicIfValid(entity)
            case Invalid(errors) => BadRequest(FailureResponse(errors.toList.map(_.errorMessage).mkString(",")))
          }

  }
}
