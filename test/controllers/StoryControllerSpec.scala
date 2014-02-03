package controllers

import play.api.test.Helpers._
import play.api.test.{FakeRequest, FakeApplication}
import models.StoryList
import org.specs2.mutable.Specification

class StoryControllerSpec extends Specification {

  "StoryController" should {

    "respond to /stories/json route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

  }

}
