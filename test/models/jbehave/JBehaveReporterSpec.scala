package models.jbehave

import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
import play.api.libs.json.Json

class JBehaveReporterSpec extends Specification with JsonMatchers {

  val id = "1394658780515"
  val reportsPath = "test/jbehave"
  val dirPath = s"$reportsPath/$id"
  val html = "public.stories.tcbdd.storiesList.html"
  val xml = "public.stories.tcbdd.storiesList.xml"
  val reports = Seq("BeforeStories.html", html, "AfterStories.html")

  "JBehaveReported#list" should {

    "return empty if ID does not exist" in {
      JBehaveReporter().list(reportsPath, "xxx").isEmpty must beTrue
    }

    "return the list of generated reports" in {
      val list = JBehaveReporter().list(reportsPath, id).get
      list must have size 3
      list must containTheSameElementsAs(reports)
    }

  }

  "JBehaveReported#steps" should {

    val steps = JBehaveReporter().steps(s"$dirPath/$xml")

    "return List" in {
      steps must beAnInstanceOf[List[Map[String, String]]]
    }

    "return List with Maps" in {
      steps(0) must beAnInstanceOf[Map[String, String]]
    }

    "return an element for each pending" in {
      steps must haveSize(6)
    }

    "return steps map with text and status keys" in {
      steps(0) must haveKeys("text", "status")
    }

  }

  "JBehaveReporter#listJson" should {

    "return empty if ID does not exist" in {
      JBehaveReporter().listJson(reportsPath, "xxx").isEmpty must beTrue
    }

    "return JSON with the report path" in {
      val json = JBehaveReporter().listJson(reportsPath, id).get.toString()
      json must */("path" -> startWith(s"/$dirPath"))
    }

    "return JSON with the list of step texts" in {
      val json = JBehaveReporter().listJson(reportsPath, id).get.toString()
      json must */("steps") */("text" -> "(Given|When|Then) .*".r)
    }

    "return JSON with the list of step statuses" in {
      val json = JBehaveReporter().listJson(reportsPath, id).get.toString()
      json must */("steps") */("status" -> "pending")
      json must */("steps") */("status" -> "successful")
      json must */("steps") */("status" -> "notPerformed")
      // TODO Add other statuses (i.e. failed)
    }

    // Does not work with files in GIT since files might not have been created in the correct order
    // TODO Generate files before the test
//    "return JSON with the list of generated reports in the correct order" in {
//      val json = JBehaveReporter().listJson(reportsPath, id).get.toString()
//      json must /("reports") /#0 /("path" -> endWith(reports(0)))
//      json must /("reports") /#1 /("path" -> endWith(reports(1)))
//      json must /("reports") /#2 /("path" -> endWith(reports(2)))
//    }

  }

}
