package controllers

import org.specs2.mutable.{After, Specification}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, FakeApplication}
import models.Composites
import play.api.libs.json.Json
import org.specs2.matcher.{PathMatchers, JsonMatchers}
import java.io.File

class CompositesControllerSpec extends Specification with JsonMatchers with PathMatchers {

  val packageName = "composites.com.technologyconversations.bdd.steps"
  val className = "TestComposites"
  val compositesDir = "composites"

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

  "GET /groovyComposites" should {

    "return OK" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/groovyComposites"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return same output as Composites#groovyClassesToJson" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/groovyComposites"))
        val composites = Composites("composites")
        contentAsJson(result) must equalTo(composites.groovyClassesToJson(composites.list()))
      }
    }

  }

  "GET /composites/*fullClassName" should {

    "return OK if fullClassName is correct" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/composites/composites.com.technologyconversations.bdd.steps.TcBddComposites"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if fullClassName is incorrect" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/composites/non.existent.class.json"))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return same output as Composites#classToJson" in {
      running(FakeApplication()) {
        val className = "composites.com.technologyconversations.bdd.steps.TcBddComposites"
        val Some(result) = route(FakeRequest(GET, s"/composites/$className"))
        status(result) must equalTo(OK)
        val composites = Composites("app/composites")
        contentAsJson(result) must equalTo(composites.classToJson(className))
      }
    }

  }

  "GET /groovyComposites/*className" should {

    "return OK if className is correct" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/groovyComposites/TcBddComposites.groovy"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
      }
    }

    "return BAD_REQUEST if className is incorrect" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/groovyComposites/NonExistentClass.groovy"))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
      }
    }

    "return same output as Composites#groovyClassToJson" in {
      running(FakeApplication()) {
        val className = "TcBddComposites.groovy"
        val Some(result) = route(FakeRequest(GET, s"/groovyComposites/$className"))
        status(result) must equalTo(OK)
        val composites = Composites("composites")
        contentAsJson(result) must equalTo(composites.groovyClassToJson(compositesDir, className))
      }
    }

  }

  "PUT /composites" should {

    val url = "/composites"
    val fakeJsonHeaders = FakeHeaders(Seq("Content-type" -> Seq("application/json")))
    val jsonMap = Map(
      "package" -> Json.toJson(packageName),
      "class" -> Json.toJson(className),
      "composites" -> Json.toJson(List[String]())
    )

    "return BAD_REQUEST if body is NOT JSON" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(PUT, url))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
        contentAsString(result) must /("message" -> noJsonResultMessage)
      }
    }

    "return BAD_REQUEST if package is not present" in {
      running(FakeApplication()) {
        val json = Json.toJson(jsonMap.filterKeys(_ != "package"))
        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, json))
        status(result) must equalTo(BAD_REQUEST)
        contentType(result) must beSome("application/json")
        contentAsString(result) must /("message" -> noResultMessage("package"))
      }
    }

    "PUT /groovyComposites" should {

      val url = "/groovyComposites"
      val fakeJsonHeaders = FakeHeaders(Seq("Content-type" -> Seq("application/json")))
      val jsonMap = Map(
        "class" -> Json.toJson(className),
        "composites" -> Json.toJson(List[String]())
      )

      "return BAD_REQUEST if body is NOT JSON" in {
        running(FakeApplication()) {
          val Some(result) = route(FakeRequest(PUT, url))
          status(result) must equalTo(BAD_REQUEST)
          contentType(result) must beSome("application/json")
          contentAsString(result) must /("message" -> noJsonResultMessage)
        }
      }

    }

      // TODO Figure out why this spec fails in Travis
//    "save file" in new AfterStoryControllerSpec(fullPath) {
//      running(FakeApplication()) {
//        val json = Json.toJson(jsonMap)
//        val Some(result) = route(FakeRequest(PUT, url, fakeJsonHeaders, json))
//        fullPath must beAnExistingPath
//        fullPath must beAFilePath
//      }
//    }

  }

  "DELETE /composites/*fullClassName" should {

    val dirPath = "app" + File.separator + packageName.replace(".", File.separator)
    val fullPath = dirPath + File.separator + className + ".java"

    "delete composites class" in new AfterCompositesControllerSpec(fullPath) {
      running(FakeApplication()) {
        val file = new File(fullPath)
        file.getParentFile.mkdirs()
        file.createNewFile
        val Some(result) = route(FakeRequest(DELETE, s"/composites/$packageName.$className"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        new File(fullPath).exists must beFalse
      }
    }

  }

  "DELETE /groovyComposites/*className" should {

    val groovyClassName = "test"
    val path = "composites" + File.separator + groovyClassName + ".groovy"

    "delete Groovy composites class" in new AfterCompositesControllerSpec(path) {
      running(FakeApplication()) {
        new File(path).createNewFile
        val Some(result) = route(FakeRequest(DELETE, s"/groovyComposites/$groovyClassName"))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        new File(path).exists must beFalse
      }
    }

  }

  class AfterCompositesControllerSpec(path: String) extends After {

    override def after = {
      val file = new File(path)
      if (file.exists) {
        file.delete
      }
    }

  }

}
