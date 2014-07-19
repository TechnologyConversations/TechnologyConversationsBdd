package controllers

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import scala.Some
import play.api.test.{FakeRequest, FakeApplication}
import org.specs2.matcher.JsonMatchers

class ReporterControllerSpec extends Specification with JsonMatchers {

  "GET /api/v1/reporters/list/[ID]" should {

    "return BAD_REQUEST if ID is not correct" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/api/v1/reporters/list/NON_EXISTING_ID"))
        status(result) must equalTo(BAD_REQUEST)
        val json = contentAsJson(result).toString()
        json must /("status" -> "ERROR")
        json must /("message" -> "ID is NOT correct")
      }
    }

  }

  "GET /api/v1/reports/get/[ID]/[REPORT]" should {

    "return BAD_REQUEST when ID or report are not correct" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/api/v1/reporters/get/NON_EXISTING_ID/NON_EXISTING_REPORT"))
        status(result) must equalTo(BAD_REQUEST)
        val json = contentAsJson(result).toString()
        json must /("status" -> "ERROR")
        json must /("message" -> "ID and/or report is NOT correct")
      }
    }

    "return status OK and type text/html when ID and report are correct" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/api/v1/reporters/get/0000000000000/public.stories.tcbdd.composites.compositeClasses.html"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
      }
    }

    "return report within an directory" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/api/v1/reporters/get/0000000000000/view/public.stories.tcbdd.composites.compositeClasses.html"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
      }
    }

  }

}
