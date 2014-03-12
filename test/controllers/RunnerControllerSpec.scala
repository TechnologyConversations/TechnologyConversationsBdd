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

  "POST /runners/run.json" should {

    val url = "/runner/run.json"
    val storyPath = "non_existent_story.story"
    val json = Json.parse(s"""{"storyPath": "$storyPath", "stepsClasses": ["com.technologyconversations.bdd.steps.WebSteps"]}""")

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
          Json.parse(s"""{"stepsClasses": [], "reportsPath": "/test/jbehave/"}""")))
        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "return BAD_REQUEST if JSON stepsClasses is not provided" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(
          POST,
          url,
          fakeJsonHeaders,
          Json.parse(s"""{"storyPath": "this_is_path", "reportsPath": "/test/jbehave/"}""")))
        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "return BAD_REQUEST if JSON stepsClasses does not have at least one value" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(
          POST,
          url,
          fakeJsonHeaders,
          Json.parse(s"""{"storyPath": "this_is_path", "stepsClasses": [], "reportsPath": "/test/jbehave/"}""")))
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
