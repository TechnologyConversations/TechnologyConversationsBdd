package controllers

import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, FakeApplication}
import org.specs2.matcher.JsonMatchers
import play.api.libs.json._

class RunnerControllerSpec extends Specification with JsonMatchers {

  val fakeJsonHeaders = FakeHeaders(Seq("Content-type" -> Seq("application/json")))

  "POST /runners/run.json" should {

    val url = "/runner/run.json"
    val storyPath = "/test/stories/**/*.story"
    val json = Json.parse(s"""{"storyPath": "$storyPath"}""")

    "return BAD_REQUEST is JSON is not provided" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(POST, url))
        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "return BAD_REQUEST is JSON storyPath is not provided" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, Json.parse(s"""{"something": "else"}""")))
        status(result) must equalTo(BAD_REQUEST)
      }
    }

//    "return JSON" in {
//      running(FakeApplication()) {
//        val Some(result) = route(FakeRequest(POST, url))
//        status(result) must equalTo(OK)
//        contentType(result) must beSome("application/json")
//        val jsonString = contentAsJson(result).as[String]
//        jsonString must /("status" -> "OK")
////        jsonString must /("storyPath" -> storyPath)
//      }
//    }

  }

}