package integration

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.tags.Slow
import play.test.WithBrowser

@Slow
class ApplicationControllerE2ETest extends AnyFlatSpec with Matchers {

  "Application" should "work from within a browser" in new WithBrowser {

    browser.goTo("http://localhost:" + port)

    browser.pageSource.contains("message") shouldBe true

  }
}
