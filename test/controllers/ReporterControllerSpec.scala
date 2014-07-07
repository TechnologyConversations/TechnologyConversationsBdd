package controllers

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import scala.Some
import play.api.test.{FakeRequest, FakeApplication}
import org.specs2.matcher.JsonMatchers

class ReporterControllerSpec extends Specification with JsonMatchers {

  "GET /api/v1/reporters/list/[ID].json" should {

    "return BAD_REQUEST if ID is not correct" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/api/v1/reporters/list/xxx"))
        status(result) must equalTo(BAD_REQUEST)
        val json = contentAsJson(result).toString()
        json must /("status" -> "ERROR")
        json must /("message" -> "ID is NOT correct")
      }
    }

  }

}
