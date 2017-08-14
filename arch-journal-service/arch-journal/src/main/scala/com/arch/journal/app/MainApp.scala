package com.arch.journal.app

import com.arch.journal.rest.JournalService
import fs2.Task
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp

/**
  * Created by williame on 7/21/17.
  */
object MainApp extends StreamApp {
  override def stream(args: List[String]): fs2.Stream[Task, Nothing] = {
    BlazeBuilder
      .bindHttp(args.head.toInt, args(1))
      .mountService(JournalService.service, "/")
      .serve
  }
}
