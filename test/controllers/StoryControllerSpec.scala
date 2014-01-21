package controllers

import play.api.test._
import play.api.test.Helpers._
import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.FakeApplication

class StoryControllerSpec extends Specification {

  "Story controller" should {

    "respond to /stories route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
      }
    }

    "respond to the index Action" in {
      running(FakeApplication()) {
        val result = StoryController.index()(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
      }
    }

  }

}
