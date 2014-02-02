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

  "Story#dirs" should {

    "return all directories" in {
      StoryUtil().dirs("test/stories") must have size 1
    }

    "return no directories when the destination directory is empty" in {
      StoryUtil().dirs("test/stories/empty") must have size 0
    }

  }

  "Story#allJson" should {

    "return JSON with all directories and stories" in {
      val storiesData = Json.toJson(Seq(
        Json.toJson(Map("name" -> Json.toJson("myStory1"))),
        Json.toJson(Map("name" -> Json.toJson("myStory2")))
      ))
      val dirsData = Json.toJson(Seq(
        Json.toJson(Map("name" -> Json.toJson("myDir1"))),
        Json.toJson(Map("name" -> Json.toJson("myDir2")))
      ))
      val expected = Json.toJson(Map("stories" -> storiesData, "dirs" -> dirsData))
      val story = new StoryUtil() {
        override def dirs(path: String) = List(
          "myDir1",
          "myDir2"
        )
        override def stories(path: String) = List(
          Story("myStory1.story"),
          Story("myStory2.story")
        )
      }
      story.allJson("mock") must be equalTo expected
    }

  }

}