package controllers

import play.api.test.Helpers._
import play.api.test.{FakeRequest, FakeApplication}
import models.StoryList
import org.specs2.mutable.Specification

class StoryControllerSpec extends Specification {

  "StoryController" should {

    "respond to /stories route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
      }
    }

    "respond to /stories/json route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
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

  "StoryController#form" should {

    "bind to Story" in {
      val data = Map("storyNameInput" -> "MyTestStory")
      StoryController.form.bind(data).get must equalTo(StoryList("MyTestStory"))
    }

    "throw errors if the input to the form does not match the constraints" in {
      val data = Map("storyNameInput" -> "")
      StoryController.form.bind(data).hasErrors must beTrue
    }

  }

}
