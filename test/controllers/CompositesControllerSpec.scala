package controllers

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.{FakeRequest, FakeApplication}
import models.Composites

class CompositesControllerSpec extends Specification {

  "GET /composites" should {

    "return OK" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/composites"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return same output as Composites#classesToJson" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/composites"))
        val composites = Composites("app/composites")
        contentAsJson(result) must equalTo(composites.classesToJson(composites.list()))
      }
    }

  }

    "GET /composites/*className" should {

    "return OK if className is correct" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/composites/composites.com.technologyconversations.bdd.steps.WebStepsComposites"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if className is incorrect" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/composites/non.existent.class.json"))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return same output as Composites#classToJson" in {
      running(FakeApplication()) {
        val className = "composites.com.technologyconversations.bdd.steps.WebStepsComposites"
        val Some(result) = route(FakeRequest(GET, s"/composites/$className"))
        status(result) must equalTo(OK)
        val composites = Composites("app/composites")
        contentAsJson(result) must equalTo(composites.classToJson(className))
      }
    }

  }

}
