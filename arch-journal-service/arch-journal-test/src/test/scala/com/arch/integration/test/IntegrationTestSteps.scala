package com.arch.integration.test

import cats.free.Free
import cats.free.Free.liftF
import cats.arrow.FunctionK
import cats.{Id, ~>}

import scala.collection.mutable
import cats.data.State
import com.arch.integration.test.IntegrationTestSteps.Params

/**
  * Created by williame on 7/26/17.
  */
sealed trait IntegrationTest[A]

case class StartService(params: Params) extends IntegrationTest[Params]

case class InvokeService(params: Params) extends IntegrationTest[Params]

case class Assert[A](expected: A, params: Params) extends IntegrationTest[Params]

case class KillService(params: Params) extends IntegrationTest[Params]

object IntegrationTestSteps {

  type Params = Map[String, Any]
  type IntTest[A] = Free[IntegrationTest, A]

  def startService(params: Params): IntTest[Params] = liftF[IntegrationTest, Params](StartService(params))
  def invokeService(params: Params): IntTest[Params] = liftF[IntegrationTest, Params](InvokeService(params))
  def assert[A](expected: A, params: Params): IntTest[Params] = liftF[IntegrationTest, Params](Assert(expected, params))
  def killService(params: Params): IntTest[Params] = liftF[IntegrationTest, Params](KillService(params))
}
