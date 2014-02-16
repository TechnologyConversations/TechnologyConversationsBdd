package controllers

import play.api.test.Helpers._
import play.api.test.{FakeRequest, FakeApplication}
import models.StoryList
import org.specs2.mutable.Specification

class StoryControllerSpec extends Specification {

  "StoryController" should {

    "respond to GET / route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/"))
        status(result) must equalTo(OK)
      }
    }

    "respond to GET /stories/list.json route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/list.json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "respond to GET /stories/story.json?path=[STORY] route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/story.json?path=story1.story"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "respond to GET /stories/story.json route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/stories/story.json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "respond to PUT /stories/story.json route" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(PUT, "/stories/story.json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

  }

}
