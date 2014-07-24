package models.jbehave

import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
import play.api.libs.json.Json
import org.specs2.mock._

class JBehaveReporterSpec extends Specification with JsonMatchers with Mockito {

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

    val steps = JBehaveReporter().steps(s"$dirPath/$xml", reportsPath, id)

    "return List" in {
      steps.get must beAnInstanceOf[List[Map[String, String]]]
    }

    "return List with Maps" in {
      steps.get(0) must beAnInstanceOf[Map[String, String]]
    }

    "return an element for each pending" in {
      steps.get must haveSize(6)
    }

    "return steps map with text and status keys" in {
      steps.get(0) must haveKeys("text", "status")
    }

  }

  "JBehaveReporter#listJson" should {

    val json = JBehaveReporter().listJson(reportsPath, id).get.toString()

    "return empty if ID does not exist" in {
      JBehaveReporter().listJson(reportsPath, "xxx").isEmpty must beTrue
    }

    "return JSON with the report path" in {
      json must */("path" -> html)
    }

    "return JSON with status" in {
      val s = spy(new JBehaveReporter)
      val status = "someStatus"
      s.status(reportsPath, id) returns status
      val actualJson = s.listJson(reportsPath, id).get.toString()
      actualJson must */("status" -> status)
    }

    "return JSON with the list of step texts" in {
      json must */("steps") */("text" -> "(Given|When|Then) .*".r)
    }

    "return JSON with the list of step statuses" in {
      json must */("steps") */("status" -> "pending")
      json must */("steps") */("status" -> "successful")
      json must */("steps") */("status" -> "notPerformed")
      // TODO Add other statuses (i.e. failed)
    }

    "JBehaveReporter#getReportContent" should {

      "return empty if report does not exist" in {
        val report = JBehaveReporter().reportContent(reportsPath, id, "non_existent_report")
        report must beNone
      }

      "return report content" in {
        val report = JBehaveReporter().reportContent(reportsPath, id, html)
        report must beSome
      }

    }

  }

  "JBehaveReporter#status" should {

    "return inProgress when view directory does not exist" in {
      val status = JBehaveReporter().status(reportsPath, "non_existent_id")
      status must equalTo("inProgress")
    }

    "return finished when view directory exists" in {
      val status = JBehaveReporter().status(reportsPath, id)
      status must equalTo("finished")
    }

  }

}

