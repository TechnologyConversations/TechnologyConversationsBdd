package controllers

import org.specs2.mutable.{After, Specification}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, FakeApplication}
import org.specs2.matcher.JsonMatchers
import play.api.libs.json._
import java.io.File
import scalax.file.Path

class RunnerControllerSpec extends Specification with JsonMatchers {

  val fakeJsonHeaders = FakeHeaders(Seq("Content-type" -> Seq("application/json")))
  val reportsPath = "public/jbehave"
  val storyPath = "non_existent_story.story"
  val json = Json.parse(s"""
    {
      "storyPath": "$storyPath",
      "classes":
      [{
        "fullName": "com.technologyconversations.bdd.steps.WebSteps",
        "params":
        [{
          "key": "key1",
          "value": "value1"
        }, {
          "key": "key2",
          "value": "value2"
        }]
      }],
      "composites":
      [{
        "package":"composites.com.technologyconversations.bdd.steps",
        "class":"TcBddComposites"
      }]
    }""")

  "POST /runners/run.json" should {

    val url = "/runner/run.json"

    "return BAD_REQUEST if JSON is not provided" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(POST, url))
        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "return BAD_REQUEST if JSON storyPath is not provided" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(
          POST,
          url,
          fakeJsonHeaders,
          Json.parse(s"""{"classes": [], "reportsPath": "/test/jbehave/"}""")))
        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "return BAD_REQUEST if JSON classes is not provided" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(
          POST,
          url,
          fakeJsonHeaders,
          Json.parse(s"""{"storyPath": "this_is_path", "reportsPath": "/test/jbehave/"}""")))
        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "return BAD_REQUEST if JSON classes does not have at least one value" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(
          POST,
          url,
          fakeJsonHeaders,
          Json.parse(s"""{"storyPath": "this_is_path", "classes": [], "reportsPath": "/test/jbehave/"}""")))
        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "run stories and return JSON" in new AfterRunnerControllerSpec {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(POST, url, fakeJsonHeaders, json))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        val jsonString = contentAsJson(result).toString()
        jsonString must /("status" -> "OK")
        jsonString must /("id" -> ".*".r)
        jsonString must /("reportsPath" -> s"$reportsPath/.*/view/reports.html".r)
      }
    }

  }

  class AfterRunnerControllerSpec extends After {

    override def after = {
      val file = new File(reportsPath)
      Path.fromString(file.getPath).deleteRecursively()
    }

  }

}

