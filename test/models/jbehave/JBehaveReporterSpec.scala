package models.jbehave

import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
import play.api.libs.json.Json

class JBehaveReporterSpec extends Specification with JsonMatchers {

  val id = "1394658780515"
  val reportsPath = "test/jbehave"
  val reports = Seq("BeforeStories.html", "public.stories.tcbdd.test.html", "AfterStories.html")

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

    "return JSON with the full path" in {
      val json = JBehaveReporter().listJson(reportsPath, id).get.toString()
      val dir = s"/$reportsPath/$id/"
      json must /("reports") */("path" -> startWith("/test/jbehave/1394658780515/"))
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
