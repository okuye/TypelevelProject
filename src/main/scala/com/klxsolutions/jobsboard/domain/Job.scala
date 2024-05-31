package com.klxsolutions.jobsboard.domain

import java.util.UUID

object job {
  case class Job(
      id: UUID,
      date: Long,
      ownerEmail: String,
      jobInfo: JobInfo,
      active: Boolean = false
  )

  case class JobInfo(
      company: String,
      title: String,
      description: String,
      externalUrl: String,
      remote: Boolean,
      location: String,
      salaryLo: Option[Int],
      salaryHi: Option[Int],
      currency: Option[String],
      country: Option[String],
      tags: Option[List[String]],
      image: Option[String],
      seniority: Option[String],
      other: Option[String]
  )

  object JobInfo {
    val empty: JobInfo = JobInfo(
      company = "",
      title = "",
      description = "",
      externalUrl = "",
      remote = false,
      location = "",
      salaryLo = None,
      salaryHi = None,
      currency = None,
      country = None,
      tags = None,
      image = None,
      seniority = None,
      other = None
    )

    def minimal(
        company: String,
        title: String,
        description: String,
        externalUrl: String,
        remote: Boolean,
        location: String
    ): JobInfo = JobInfo(
      company,
      title,
      description,
      externalUrl,
      remote,
      location,
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )
  }
}
