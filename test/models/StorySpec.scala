package models

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import org.joda.time.DateTime
import scala.io.Source
import org.jbehave.core.model.Narrative

class StorySpec extends Specification {

  "StoryList" should {

    "have name without extension" in {
      StoryList("myStory.story").name must be equalTo "myStory"
    }

  }

  "Story" should {

    val narrative = "Narrative:\nIn order to do something\nAs a someone\nI want to be able to"
    val scenario1 = "Scenario: My first scenario\nGiven some precondition\nWhen some action\nThen some outcome"
    val scenario2 = "Scenario: My second scenario\nGiven some precondition\nWhen some action\nThen some outcome"
    val storyContent = narrative + "\n\n" + scenario1 + "\n\n" + scenario2

    "have name without extension" in {
      val story = Story("myStory.story", storyContent)
      story.name must be equalTo "myStory"
    }

    "have content" in {
      val story = Story("myStory.story", storyContent)
      story.content must be equalTo storyContent
    }

    "have narrative" in {
      val story = Story("myStory.story", storyContent)
      story.jBehaveStory.getNarrative.inOrderTo must be equalTo "do something"
      story.jBehaveStory.getNarrative.asA must be equalTo "someone"
      story.jBehaveStory.getNarrative.iWantTo must be equalTo "be able to"
    }

    "have narrative empty when not provided in the story" in {
      val story = Story("myStory.story", scenario1 + "\n\n" + scenario2)
      story.jBehaveStory.getNarrative.isEmpty must be equalTo true
    }

  }

  "StoryUtil#fileSource" should {

    "return contents of the specified file" in {
      val actual = StoryUtil().fileSource("test/stories/story1.not_story")
      actual must equalTo("This is\nnot a\nstory file")
    }

  }

  "StoryUtil#story" should {

    val storyUtil = new StoryUtil() {
      override def fileSource(path: String):String = {
        "This is some content"
      }
    }

    "return Story with name" in {
      storyUtil.story("myStory.story").name must be equalTo "myStory"
    }

    "return Story with file content" in {
      storyUtil.story("myStory.story").content must be equalTo "This is some content"
    }

  }

  "StoryUtil#stories" should {

    "return all stories from files ending with .story" in {
      StoryUtil().stories("test/stories") must have size 3
    }

    "return no stories when the destination directory is empty" in {
      StoryUtil().stories("test/stories/empty") must have size 0
    }

  }

  "StoryUtil#dirs" should {

    "return all directories" in {
      StoryUtil().dirs("test/stories") must have size 1
    }

    "return no directories when the destination directory is empty" in {
      StoryUtil().dirs("test/stories/empty") must have size 0
    }

  }

  "StoryUtil#allJson" should {

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
      val storyUtil = new StoryUtil() {
        override def dirs(path: String) = List(
          "myDir1",
          "myDir2"
        )
        override def stories(path: String) = List(
          StoryList("myStory1.story"),
          StoryList("myStory2.story")
        )
      }
      storyUtil.allJson("mock") must be equalTo expected
    }

  }

}