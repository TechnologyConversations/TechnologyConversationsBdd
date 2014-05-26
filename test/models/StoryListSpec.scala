package models

import org.specs2.mutable.Specification
import play.api.libs.json.Json

class StoryListSpec extends Specification {

  "StoryList#stories" should {

    "return all stories from files ending with .story" in {
      StoryList("test/stories").stories must have size 3
    }

    "return no stories when the destination directory is empty" in {
      StoryList("test/stories/empty").stories must have size 0
    }

  }

  "StoryList#dirs" should {

    "return all directories" in {
      StoryList("test/stories").dirs must have size 1
    }

    "return no directories when the destination directory is empty" in {
      StoryList("test/stories/empty").dirs must have size 0
    }

  }

  "StoryList#json" should {

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
      val storyList = new StoryList("mock") {
        override def dirs = List(
          "myDir1",
          "myDir2"
        )
        override def stories = List(
          "myStory1",
          "myStory2"
        )
      }
      storyList.json must be equalTo expected
    }

  }

}
