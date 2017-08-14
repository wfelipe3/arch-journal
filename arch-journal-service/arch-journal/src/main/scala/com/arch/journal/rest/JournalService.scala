package com.arch.journal.rest

import org.http4s._, org.http4s.dsl._

/**
  * Created by williame on 7/21/17.
  */
object JournalService {

  val service = HttpService {
    case request @ GET -> Root / "journal" =>
      Ok("[]")
  }

}
