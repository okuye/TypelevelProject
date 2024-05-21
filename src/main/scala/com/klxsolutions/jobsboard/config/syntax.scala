package com.klxsolutions.jobsboard.config

import pureconfig.ConfigSource
import pureconfig.ConfigReader
import cats.MonadThrow
import pureconfig.error.ConfigReaderException
import scala.reflect.ClassTag
import cats.FlatMap.nonInheritedOps.toFlatMapOps

object syntax {
  extension (source : ConfigSource)
    def loadF[F[_], A](using reader: ConfigReader[A], F: MonadThrow[F], tag: ClassTag[A]) : F[A] = 
        F.pure(source.load[A]).flatMap {
                case Left(errors) => F.raiseError[A](ConfigReaderException(errors))
                case Right(value) => F.pure(value)
        }
}
