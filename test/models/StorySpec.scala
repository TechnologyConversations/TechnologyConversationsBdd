package models

import java.util.NoSuchElementException

import models.db.BddDb
import org.specs2.mutable.Specification
import scala.Predef._
import play.api.libs.json.Json
import org.jbehave.core.model.{Narrative, Lifecycle}
import models.jbehave.JBehaveStoryMock
import org.specs2.mock._

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

    "call upsertStory" in {
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb))
      story.saveStory()
      there was one(bddDb).upsertStory()
    }

    "NOT throw exception when bddDb is empty" in {
      val story = new Story(bddDb = Option.empty)
      story.saveStory() must not(throwA[NoSuchElementException])
    }

    "should return false when upsertStory is false" in {
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb))
      bddDb.upsertStory() returns false
      story.saveStory() must beFalse
    }

    "should return true when upsertStory is true" in {
      val bddDb = mock[BddDb]
      val story = new Story(bddDb = Option(bddDb))
      bddDb.upsertStory() returns true
      story.saveStory() must beTrue
    }

  }

}