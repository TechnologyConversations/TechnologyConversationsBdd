package models

import models.db.BddDb
import models.file.BddFile
import org.specs2.mutable.Specification
import scala.Predef._
import play.api.libs.json.Json
import org.jbehave.core.model.{Narrative, Lifecycle}
import models.jbehave.JBehaveStoryMock
import org.specs2.mock._
import java.io.File
import com.mongodb.casbah.Imports._

class StorySpec extends Specification with Mockito {

  "Story#rootCollection" should {

    "have empty name when path is an empty string" in {
      new Story("", "").rootCollection must havePair("name" -> Json.toJson(""))
    }

    "have empty description when path is an empty string" in {
      new Story("", "").rootCollection must havePair("description" -> Json.toJson(""))
    }

    "have empty meta when path is an empty string" in {
      new Story("", "").rootCollection must havePair("meta" -> Json.arr())
    }

    "have empty givenStories when path is an empty string" in {
      new Story("", "").rootCollection must havePair("givenStories" -> Json.arr())
    }

    "have empty scenarios when path is an empty string" in {
      new Story("", "").rootCollection must havePair("scenarios" -> Json.arr())
    }

    "have lifecycle with empty before and after when path is an empty string" in {
      new Story("", "").rootCollection must havePair("lifecycle" -> Json.toJson(JBehaveStoryMock.lifecycleCollection(new Lifecycle())))
    }

    "have narrative with empty inOrderTo, asA and iWantTo when path is an empty string" in {
      val narrative = new Narrative("", "", "")
      new Story("", "").rootCollection must havePair("narrative" -> Json.toJson(JBehaveStoryMock.narrativeCollection(narrative)))
    }

  }

  "Story object" should {

    "return Story instance when apply is called" in {
      Story("test", "stories/story1.story") must beAnInstanceOf[Story]
    }

  }

  "Story#saveStory" should {

    val file = mock[File]
    val overwrite = true
    val json = Json.parse(storyJsonString)

    "call upsertStory" in {
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb))
      story.saveStory(file, json, overwrite)
      there was one(bddDb).upsertStory(any[MongoDBObject], any[MongoDBObject])
    }

    "NOT call bddDb when empty" in {
      val bddDb = mock[Option[BddDb]]
      new Story(bddDb = bddDb)
      there was no(bddDb).get
    }

    "should return false when upsertStory is false" in {
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb))
      bddDb.upsertStory(any[MongoDBObject], any[MongoDBObject]) returns false
      story.saveStory(file, json, overwrite) must beFalse
    }

    "call saveFile" in {
      val bddFile = mock[BddFile]
      val story = new Story(bddFile = Option(bddFile))
      story.saveStory(file, json, overwrite)
      there was one(bddFile).saveFile(file, story.toText(json), overwrite = true)
    }

    "NOT call bddFile when empty" in {
      val bddFile = mock[Option[BddFile]]
      new Story(bddFile = bddFile)
      there was no(bddFile).get
    }

    "should return false when saveFile is false" in {
      val bddFile = mock[BddFile]
      val story = new Story(bddFile = Option(bddFile))
      bddFile.saveFile(file, story.toText(json), overwrite) returns false
      story.saveStory(file, json, overwrite) must beFalse
    }

    "should return true when upsertStory and saveFile are true" in {
      val bddDb = mock[BddDb]
      val bddFile = mock[BddFile]
      val story = new Story(bddDb = Option(bddDb), bddFile = Option(bddFile))
      bddDb.upsertStory(any[MongoDBObject], any[MongoDBObject]) returns true
      bddFile.saveFile(file, story.toText(json), overwrite) returns true
      story.saveStory(file, json, overwrite) must beTrue
    }

  }

  lazy val storyJsonString =
    """
{
  "path": "my_test_story.story",
  "name": "my_test_story",
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