package controllers


import cats.effect.IO
import com.google.inject.Guice
import dao.{DaoModule, ProductDAO}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.tags.Slow
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Slow
class ApplicationControllerTest extends AnyFlatSpec with Matchers {

  val application: Application = GuiceApplicationBuilder().build()

  "Application" should "send 404 on a bad request" in {
    val res = Await.result(route(application, FakeRequest(GET, "/boum")).get, Duration.Inf)
    res.header.status shouldBe 404
  }

  it should "render the index page" in {
    val home = route(application, FakeRequest(GET, "/")).get

    status(home) shouldBe OK
    contentType(home) shouldBe Some("application/json")
  }
}
