package controllers

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import scala.Some
import play.api.test.{FakeRequest, FakeApplication}
import models.Steps

class StepsControllerSpec extends Specification {

  "GET /steps/list.json" should {

    "return JSON" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/steps/list.json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return JSON from Steps#stepsToJson" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/steps/list.json"))
        contentAsJson(result) must beEqualTo(Steps().stepsToJson)
      }
    }

  }

  "GET /steps/classes.json" should {

    "return JSON" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/steps/classes.json"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return JSON from Steps#classesToJson" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/steps/classes.json"))
        contentAsJson(result) must beEqualTo(Steps().classesToJson)
      }
    }

  }

}
