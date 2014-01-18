package controllers

import org.specs2.mutable.Specification
import play.api.test._
import play.api.test.Helpers._
import org.specs2.runner._
import org.specs2.mutable._

class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/babum")) must beNone
      }
    }

  }

}
