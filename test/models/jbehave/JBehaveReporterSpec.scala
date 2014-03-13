package models.jbehave

import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
import play.api.libs.json.Json

class JBehaveReporterSpec extends Specification with JsonMatchers {

  val id = "1394658780515"
  val reportsPath = "test/jbehave"
  val reports = Seq("AfterStories.html", "BeforeStories.html", "public.stories.tcbdd.test.html")

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

  "JBehaveReporter#listJson" should {

    "return empty if ID does not exist" in {
      JBehaveReporter().listJson(reportsPath, "xxx").isEmpty must beTrue
    }

    "return the JSON with the list of generated reports" in {
      val json = JBehaveReporter().listJson(reportsPath, id).get.toString()
      json must /("reports") */("report" -> reports(0))
      json must /("reports") */("report" -> reports(1))
      json must /("reports") */("report" -> reports(2))
    }

  }

}
