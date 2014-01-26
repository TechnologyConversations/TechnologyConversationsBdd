package models

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.libs.json.Json

class StorySpec extends Specification {

  "Story" should {

    "have name without extension" in {
      Story("myStory.story").name must be matching "myStory"
    }

  }

  "Story#stories" should {

    "return all stories from files ending with .story" in {
      StoryUtil().stories("test/stories") must have size 3
    }

    "return no stories when the destination directory is empty" in {
      StoryUtil().stories("test/stories/empty") must have size 0
    }

  }

  "Story#all" should {

    "return all stories and directories" in {
      StoryUtil().all("test/stories") must have size 4
    }

    "return no stories when the destination directory is empty" in {
      StoryUtil().all("test/stories/empty") must have size 0
    }

  }

  "Story#dirs" should {

    "return all directories" in {
      StoryUtil().dirs("test/stories") must have size 1
    }

    "return no directories when the destination directory is empty" in {
      StoryUtil().dirs("test/stories/empty") must have size 0
    }

  }

  "Story#jsTree" should {

    "return JSON with all stories and directories" in {
      val expected = Json.toJson(Seq(
          Json.toJson(Map("text" -> Json.toJson("myDir1"))),
          Json.toJson(Map("text" -> Json.toJson("myStory1.story")))
      ))
      val story = new StoryUtil() {
        override def all(path: String): List[String] = List("myDir1", "myStory1.story")
      }
      story.jsTree("mock") must be equalTo expected
    }

  }

}