package controllers

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import scala.Some
import play.api.test.{FakeRequest, FakeApplication}
import org.specs2.matcher.JsonMatchers

class ReporterControllerSpec extends Specification with JsonMatchers {

  val url = "/reporters/list/1394658780515.json"
  val report = "public.stories.tcbdd.test.html"

  "GET /reporters/list/[ID].json" should {

    "return BAD_REQUEST if ID is not correct" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/reporters/list/xxx.json"))
        status(result) must equalTo(BAD_REQUEST)
        val json = contentAsJson(result).toString()
        json must /("status" -> "ERROR")
        json must /("message" -> "ID is NOT correct")
      }
    }

    "return JSON with reports" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, url))
        status(result) must equalTo(OK)
        val json = contentAsJson(result).toString()
        json must /("reports") */("report" -> report)
      }
    }

  }

}
