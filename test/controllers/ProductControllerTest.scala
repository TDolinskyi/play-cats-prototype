package controllers

import model.ProductData
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.tags.Slow
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test._

@Slow
class ProductControllerTest extends AnyFlatSpec with Matchers {
  val application: Application = GuiceApplicationBuilder().build()

  "ProductController" should "get product" in {
      val rout = route(application, FakeRequest(GET, "/products")).get
      contentAsJson(rout).as[Seq[ProductData]] should not be empty
  }
}
