package views

import play.api.test.Helpers._
import play.api.test._
import org.specs2.mutable._

class PageNotFoundSpec extends Specification {

  "Page not found" should {

    "be displayed when /pageNotFound is requested" in new WithBrowser {
      browser.goTo("/pageNotFound")
      browser.title() must equalTo("Page Not Found")
      browser.$("#title").getText must equalTo("Seems that the page you're looking for went for a walk and could not be found ever since.")
    }

  }

}
