package models

import models.db.BddDb
import models.file.BddFile
import org.specs2.matcher.JsonMatchers
import org.specs2.mutable.Specification
import scala.Predef._
import play.api.libs.json.{JsValue, Json}
import org.jbehave.core.model.{Narrative, Lifecycle}
import models.jbehave.JBehaveStoryMock
import org.specs2.mock._
import java.io.File

class StorySpec extends Specification with Mockito with JsonMatchers {

  val storyName = "my_fancy"
  val storyDirPath = "path/to/"
  val storyPath = s"$storyDirPath$storyName.story"
  val storyJson = Json.parse(storyJsonString)

  "Story#rootCollection" should {

    val storyName = "my.story"
    val rootCollection = Story().rootCollection(storyName, "path/to/my.story", "")

    "have empty name when path is an empty string" in {
      rootCollection must havePair("name" -> Json.toJson(storyName))
    }

    "have empty description when path is an empty string" in {
      rootCollection must havePair("description" -> Json.toJson(""))
    }

    "have empty meta when path is an empty string" in {
      rootCollection must havePair("meta" -> Json.arr())
    }

    "have empty givenStories when path is an empty string" in {
      rootCollection  must havePair("givenStories" -> Json.arr())
    }

    "have empty scenarios when path is an empty string" in {
      rootCollection  must havePair("scenarios" -> Json.arr())
    }

    "have lifecycle with empty before and after when path is an empty string" in {
      rootCollection  must havePair("lifecycle" -> Json.toJson(JBehaveStoryMock.lifecycleCollection(new Lifecycle())))
    }

    "have narrative with empty inOrderTo, asA and iWantTo when path is an empty string" in {
      val narrative = new Narrative("", "", "")
      rootCollection  must havePair("narrative" -> Json.toJson(JBehaveStoryMock.narrativeCollection(narrative)))
    }

  }

  "Story object" should {

    "return Story instance when apply is called" in {
      Story("test", "stories/story1.story") must beAnInstanceOf[Story]
    }

  }

  "Story#saveStory" should {

    val overwrite = true

    // TODO Remove
    "have upsertStory disabled by feature toggles" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb))
      story.saveStory(file, storyJson, overwrite)
      there was no(bddDb).upsertStory(any[JsValue])
    }

    "call upsertStory" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb)) {
        override val mongoDbIsEnabled = true
      }
      story.saveStory(file, storyJson, overwrite)
      there was one(bddDb).upsertStory(storyJson)
    }

    "NOT call bddDb when empty" in {
      val bddDbOption = mock[Option[BddDb]]
      new Story(bddDb = bddDbOption)
      there was no(bddDbOption).get
    }

    "should return false when upsertStory is false" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb)) {
        override val mongoDbIsEnabled = true
      }
      bddDb.upsertStory(any[JsValue]) returns false
      story.saveStory(file, storyJson, overwrite) must beFalse
    }

    "call saveFile" in {
      val file = mock[File]
      val bddFile = mock[BddFile]
      val story = new Story(bddFile = Option(bddFile))
      story.saveStory(file, storyJson, overwrite)
      there was one(bddFile).saveFile(file, story.toText(storyJson), overwrite = true)
    }

    "NOT call bddFile when empty" in {
      val bddFile = mock[Option[BddFile]]
      new Story(bddFile = bddFile)
      there was no(bddFile).get
    }

    "should return false when saveFile is false" in {
      val file = mock[File]
      val bddFile = mock[BddFile]
      val story = new Story(bddFile = Option(bddFile))
      bddFile.saveFile(file, story.toText(storyJson), overwrite) returns false
      story.saveStory(file, storyJson, overwrite) must beFalse
    }

    "should return true when upsertStory and saveFile are true" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val bddFile = mock[BddFile]
      val story = new Story(bddDb = Option(bddDb), bddFile = Option(bddFile))
      bddDb.upsertStory(any[JsValue]) returns true
      bddFile.saveFile(file, story.toText(storyJson), overwrite) returns true
      story.saveStory(file, storyJson, overwrite) must beTrue
    }

  }

  "Story#deleteStory" should {

    // TODO Remove
    "have BddDb#removeStory disabled by feature toggles" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb))
      story.removeStory(file, storyPath)
      there was no(bddDb).removeStory(storyPath)
    }

    "call BddFile#deleteFile" in {
      val file = mock[File]
      val bddFile = mock[BddFile]
      val story = new Story(bddFile = Option(bddFile))
      story.removeStory(file, storyPath)
      there was one(bddFile).deleteFile(file)
    }

    "NOT call BddFile#deleteFile when option is empty" in {
      val file = mock[File]
      val bddFileOption = mock[Option[BddFile]]
      val story = new Story(bddFile = bddFileOption)
      story.removeStory(file, storyPath)
      there was no(bddFileOption).get
    }

    "return false when file was NOT deleted" in {
      val file = mock[File]
      val bddFile = mock[BddFile]
      val story = new Story(bddFile = Option(bddFile))
      bddFile.deleteFile(file) returns false
      story.removeStory(file, storyPath) must beFalse
    }

    "call BddDb#removeStory" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb)) {
        override val mongoDbIsEnabled = true
      }
      story.removeStory(file, storyPath)
      there was one (bddDb).removeStory(storyPath)
    }

    "NOT call BddDb#removeStory when option is empty" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val bddDbOption = mock[Option[BddDb]]
      val story = new Story(bddDb = Option(bddDb)) {
        override val mongoDbIsEnabled = true
      }
      story.removeStory(file, storyPath)
      there was no(bddDbOption).get
    }

    "return false when story was NOT removed from the DB" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb)) {
        override val mongoDbIsEnabled = true
      }
      bddDb.removeStory(storyPath) returns false
      story.removeStory(file, storyPath) must beFalse
    }

    "return true when file was deleted and removed from the DB" in {
      val file = mock[File]
      val bddDb = mock[BddDb]
      val bddFile = mock[BddFile]
      val story = new Story(bddFile = Option(bddFile), bddDb = Option(bddDb)) {
        override val mongoDbIsEnabled = true
      }
      bddDb.removeStory(storyPath) returns true
      bddFile.deleteFile(file) returns true
      story.removeStory(file, storyPath) must beTrue
    }

  }

  "Story#findStory" should {

    val file = mock[File]

    // TODO Remove
    "have BddDb#findStory disabled by feature toggles" in {
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb))
      story.findStory(file, storyPath)
      there was no(bddDb).findStory(storyPath)
    }

    "not call BddDb#findStory when option is empty" in {
      val bddDbOption = mock[Option[BddDb]]
      val story = new Story(bddDb = bddDbOption) {
        override val mongoDbIsEnabled = true
      }
      story.findStory(file, storyPath)
      there was no(bddDbOption).get
    }

    "return JSON from DB when BddDb is defined" in {
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb)) {
        override val mongoDbIsEnabled = true
      }
      bddDb.findStory(storyPath) returns Option(storyJson)
      story.findStory(file, storyPath) must beEqualTo(Option(storyJson))
    }

    "return JSON from file when BddDb is empty" in {
      val bddFile = mock[BddFile]
      val story = new Story(bddFile = Option(bddFile))
      file.getName returns s"$storyName.story"
      bddFile.fileToString(file) returns Option(storyString)
      story.findStory(file, storyPath)
      story.findStory(file, storyPath).get must beEqualTo(storyJson)
    }

    "return empty when both BddDb and BddFile are empty" in {
      val story = new Story()
      val emptyStory = Option(story.storyToJson("", storyPath, ""))
      story.findStory(file, storyPath) must beEqualTo(emptyStory)
    }

  }

  "Story#findStories" should {

    val dir1Name = "myDir1"
    val dir2Name = "myDir2"
    val story1Name = "myStory1.story"
    val story2Name = "myStory2.story"
    val dir = mock[File]
    val bddFile = mock[BddFile]
    bddFile.listDirs(dir) returns List(dir1Name, dir2Name)
    bddFile.listFiles(dir) returns List(story1Name, story2Name)

    "return empty option when both BddFile and BddDb are empty" in {
      val file = mock[File]
      val story = new Story()
      story.findStories(file, "PATH") must equalTo(Option.empty)
    }

    "return JSON with all directories from FS" in {
      val story = new Story(bddFile = Option(bddFile))
      val json = story.findStories(dir, "PATH").get.toString()
      json must /("dirs") */("name" -> dir1Name)
      json must /("dirs") */("name" -> dir2Name)
    }

    "return JSON with all story files from FS" in {
      val story = new Story(bddFile = Option(bddFile))
      val json = story.findStories(dir, "PATH").get.toString()
      println(json)
      json must /("stories") */("name" -> story1Name)
      json must /("stories") */("name" -> story2Name)
    }

  }


  val storyString =
    """
This is description of this story

Meta:
@integration
@product dashboard

Narrative:
In order to communicate effectively to the business some functionality
As a development team
I want to use Behaviour-Driven Development

GivenStories: story.story

Lifecycle:

Before:
Given a step that is executed before each scenario

After:
Given a step that is executed after each scenario

Scenario: A scenario is a collection of executable steps of different type

Meta:
@live
@product shopping cart

Given step represents a precondition to an event

Examples:
|precondition|be-captured|
|abc|be captured|
|xyz|not be captured|
""".stripMargin
  lazy val storyJsonString =
    """
{
  "dirPath": "path/to/",
  "path": "path/to/my_fancy.story",
  "name": "my_fancy",
  "description": "This is description of this story",
  "meta": [ { "element": "integration" }, { "element": "product dashboard" } ],
  "narrative":
  {
    "inOrderTo": "communicate effectively to the business some functionality",
    "asA": "development team",
    "iWantTo": "use Behaviour-Driven Development"
  },
  "givenStories": [ { "story": "story.story" } ],
  "lifecycle":
  {
    "before": [ { "step": "Given a step that is executed before each scenario" } ],
    "after": [ { "step": "Given a step that is executed after each scenario" } ]
  },
  "scenarios":
  [
    {
      "title": "A scenario is a collection of executable steps of different type",
      "meta": [ { "element": "live" }, { "element": "product shopping cart" } ],
      "steps": [ { "step": "Given step represents a precondition to an event" } ],
      "examplesTable": "|precondition|be-captured|\n|abc|be captured|\n|xyz|not be captured|"
    }
  ]
}""".stripMargin

}
