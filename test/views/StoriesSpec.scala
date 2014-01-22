package views

import play.api.test._
import org.specs2.mutable._
import models.Story

class StoriesSpec extends Specification {

  "Page stories" should {

    "have the correct title" in new WithBrowser {
      browser.goTo("/stories")
      browser.title() must equalTo("Stories")
    }

    "display the list of stories" in new WithBrowser {
      (1 to 3).foreach(index => Story.create(Story("mystory" + index)))
      browser.goTo("/stories")
      browser.find("#storyListTitle").getText must equalTo("3 stories")
      browser.find(".storyListName").size() must equalTo(3)
      (0 until 3).foreach(
        index => browser.find(".storyListName", index).getText must equalTo("mystory" + (index + 1))
      )
    }

    "have buttons to delete specific story" in new WithBrowser {
      (1 to 3).foreach(index => Story.create(Story("mystory" + index)))
      browser.goTo("/stories")
      val delete = browser.find(".storyListDelete", 1)
      delete.getValue must equalTo("Delete")
      delete.click
      browser.find(".storyListName").size() must equalTo(2)
      browser.find("#list").getText must not contain "mystory2"
    }

    "have the form to add a new story" in new WithBrowser {
      browser.goTo("/stories")
      browser.find("#storyAddTitle").getText must equalTo("Add New Story")
      browser.find("#storyNameInput").text("My New Story")
      browser.find("#storyCreate").click
      browser.find(".storyListName", 0).getText must equalTo("My New Story")
    }

  }

}
