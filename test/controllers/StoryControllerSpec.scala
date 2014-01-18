package controllers

import play.api.test._
import org.specs2.mutable._
import play.api.test.Helpers._

class StoryControllerSpec extends Specification {

  "StoryController#index" should {

    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }

  }

}