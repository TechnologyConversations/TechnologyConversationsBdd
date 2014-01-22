package controllers

import play.api.test._
import play.api.test.Helpers._
import org.specs2.mutable._
import play.api.test.FakeApplication
import models.Story

class StoryControllerSpec extends Specification {

  "StoryController" should {

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

  "StoryController#form" should {

    "bind to Story" in {
      val data = Map("storyNameInput" -> "MyTestStory")
      StoryController.form.bind(data).get must equalTo(Story("MyTestStory"))
    }

    "throw errors if the input to the form does not match the constraints" in {
      val data = Map("storyNameInput" -> "")
      StoryController.form.bind(data).hasErrors must beTrue
    }

  }

}
