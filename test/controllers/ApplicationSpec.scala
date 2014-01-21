package controllers

import play.api.test._
import play.api.test.Helpers._
import org.specs2.mutable._

class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/babum")) must beNone
      }
    }

    "redirect / to /stories" in {
      running(FakeApplication()) {
        val result = route(FakeRequest(GET, "/")).get
        redirectLocation(result) must beSome.which(_ == "/stories")
      }
    }

  }

}
