package views

import play.api.test._
import org.specs2.mutable._
import models.Story

class StoriesSpec extends Specification {

  // TODO Finish tests
  "Page stories" should {

    "have the correct title" in new WithBrowser {
      browser.goTo("/stories")
      browser.title() must equalTo("Stories")
    }

  }

}
