// package com.klxsolutions.jobsboard.logging
// import cats.*
// import cats.implicits.*
// import org.typelevel.log4cats.Logger

// object syntax {
//   // Properly specify that `MonadError` is for effects `F` with error type `E`
//   extension [F[_], E, A](fa: F[A])(using me: MonadError[F, E], logger: Logger[F]) {
//     def log(success: A => String, error: E => String): F[A] = fa.attemptTap {
//       case Left(e: E) => logger.error(error(e)) // Explicitly cast `e` to `E` if not inferred
//       case Right(a)   => logger.info(success(a))
//     }

//     def logError(error: E => String): F[A] = fa.attemptTap {
//       case Left(e: E) => logger.error(error(e))
//       case Right(a)   => ().pure[F]
//     }
//   }
// }
package com.klxsolutions.jobsboard.logging

import cats.*
import cats.implicits.*
import org.typelevel.log4cats.Logger

object syntax {
  extension [F[_], A](fa: F[A])(using MonadThrow[F], Logger[F]) {
    def log(success: A => String, error: Throwable => String): F[A] = fa.attemptTap {
      case Left(e)  => Logger[F].error(error(e))
      case Right(a) => Logger[F].info(success(a))
    }

    def logError(error: Throwable => String): F[A] = fa.attemptTap {
      case Left(e)  => Logger[F].error(error(e))
      case Right(_) => ().pure[F]
    }
  }
}
