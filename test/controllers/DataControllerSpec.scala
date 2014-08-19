package controllers

import org.specs2.matcher.JsonMatchers
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeApplication, FakeRequest}

import scala.io.Source

class DataControllerSpec extends Specification with JsonMatchers {

  val url = "/api/v1/data/features"
  val nonExistingUrl = "/api/v1/data/NON_EXISTENT_ID"

  "get" should {

    "return JSON with status OK" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, url))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return JSON should have data from the specified file" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, url))
        val actual = contentAsString(result)
        val expectedJson = Json.parse(Source.fromFile("public/data/features.json").mkString)
        val expected = toJson(data = Option(expectedJson)).toString()
        actual must equalTo(expected)
      }
    }

    "return JSON with status not found (404) when data does not exist" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, nonExistingUrl))
        status(result) must equalTo(NOT_FOUND)
        contentType(result) must beSome("application/json")
      }
    }

    "return JSON with error and status not found (404) when data does not exist" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, nonExistingUrl))
        val json = contentAsString(result)
        json must /("meta") /("error" -> "ID NON_EXISTENT_ID could not be found".r)
      }
    }

  }

  "data directory" should {

    "contain heroku for each data file" in {
      val directory = new java.io.File("public/data")
      val files = directory.list().toList
        .filter(!_.contains("_heroku"))
      val herokuFiles = files
        .map(_.replace(".json", ""))
        .map(name => s"${name}_heroku.json")
      directory.list().toList must containTheSameElementsAs(files ++ herokuFiles)
    }

  }

}
