package com.arch.integration.test

import cats.implicits._
import com.arch.integration.test.IntegrationTestSteps.{assert, invokeService, killService, startService}
import com.google.common.collect.ImmutableMap
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.messages.{ContainerConfig, HostConfig, PortBinding}
import hammock.circe.implicits._
import hammock.hi._
import hammock.jvm.free.Interpreter
import hammock.{Hammock, Uri, _}
import org.scalatest._
import cats.{Id, ~>}

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try

/**
  * Created by williame on 7/20/17.
  */
@WrapWith(classOf[ConfigMapWrapperSuite])
class ArchJournalTest(configMap: Map[String, Any]) extends FreeSpec with Matchers {

  type ExecResult[A] = Either[String, A]
  implicit val interpreter = Interpreter()

  "given invoke to get all journals" - {
    "when there are not journals" - {
      "then return empty" in {

        val test = for {
          p1 <- startService(Map("journal.image" -> "default/arch-journal", "journal.port" -> "8080"))
          p2 <- invokeService(p1)
          p3 <- IntegrationTestSteps.assert(List.empty[String], p2)
          p4 <- killService(p3)
        } yield p4


        createJournalService(Map("journal.image" -> "default/arch-journal", "journal.port" -> "8080"))
          .flatMap(invokeJournalService)
          .flatMap(createAssertion(List.empty[String]))
          .flatMap(killJournalService)
          .fold(e => fail(e), p => {
            val (actual, expected) = p("journal.assertion").asInstanceOf[(List[String], List[String])]
            actual should be(expected)
          })
      }
    }
  }

  def compiler(): IntegrationTest ~> Id = new (IntegrationTest ~> Id) {
    override def apply[A](fa: IntegrationTest[A]): Id[A] = fa match {
      case StartService(params) => createJournalService(params)
    }
  }

  def createJournalService(params: Map[String, Any]): ExecResult[Map[String, Any]] = {
    val container: Try[Container] = for {
      docker <- Try(new DefaultDockerClient("unix:///var/run/docker.sock"))
      container <- createContainerIdempotent(params, docker)
    } yield startContainer(docker, container)

    toEither(container)
      .map(c => params + ("journal.url" -> "http://localhost:8080/journal") + ("journal.container" -> c))
  }

  private def createContainerIdempotent(params: Map[String, Any], docker: DefaultDockerClient) = Try {
    docker.listContainers().asScala
      .find(c => c.image() == params("journal.image"))
      .map(c => Container(c.id(), c.state()))
      .getOrElse(createContainer(params, docker))
  }

  private def killJournalService(params: Map[String, Any]) = {
    val docker = new DefaultDockerClient("unix:///var/run/docker.sock")
    val id = params("journal.container").asInstanceOf[Container].id
    docker.killContainer(id)
    docker.removeContainer(id)
    Right(params)
  }

  def createContainer(params: Map[String, Any], docker: DefaultDockerClient): Container = {
    val container = docker.createContainer(
      ContainerConfig.builder()
        .image(params("journal.image").asInstanceOf[String])
        .hostConfig(HostConfig
          .builder()
          .portBindings(ImmutableMap.of(s"${params("journal.port")}/tcp", java.util.Arrays.asList(PortBinding.of("", params("journal.port").asInstanceOf[String]))))
          .build()
        )
        .exposedPorts(s"${params("journal.port")}/tcp")
        .build
    )

    Container(container.id(), "created")
  }

  def startContainer(docker: DefaultDockerClient, container: Container) =
    if (container.state != "running") {
      docker.startContainer(container.id)
      Thread.sleep(1000)
      container.copy(state = "running")
    } else {
      container
    }

  def invokeJournalService(params: Map[String, Any]): ExecResult[Map[String, Any]] = {

    def invoke(id: String) = Future {
      Hammock.deleteWithOpts(Uri.unsafeParse(s"http://run.bizagi.com/api/Workspace/$id"), Opts.default)
        .exec[Try].as[Map[String, Any]
        ]
    }

    val futures: List[Future[Try[Map[String, Any]]]] = List("lkajsdklfjas", "xkjdflksjdf", "skdfjlksdfjklsj").map(id => {
      invoke(id)
    })


    Await.result(Future.sequence(futures), Duration.Inf)


    val res = Hammock.getWithOpts(Uri.unsafeParse(params("journal.url").asInstanceOf[String]), Opts.default).exec[Try].as[List[String]]
    toEither(res).map(r => params + ("journal.response" -> r))
  }

  def createAssertion(expected: List[String])(params: Map[String, Any]) = {
    Right(params + ("journal.assertion" -> (params("journal.response"), expected)))
  }

  def toEither[A](a: Try[A]): ExecResult[A] =
    a.map(Right(_))
      .recover { case e: Exception => Left(e.getMessage) }
      .get

  case class Container(id: String, state: String)

}
