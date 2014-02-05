package models

import org.specs2.mutable.Specification
import java.io.FileNotFoundException
import org.specs2.matcher.JsonMatchers

class StorySpec extends Specification with JsonMatchers {

  "Story#name" should {

    "return story name without extension" in {
      val story = new Story("myStory.story")
      story.name must be equalTo "myStory"
    }

  }

  "Story#content" should {

    "return content of the story" in {
      val story = new Story("test/stories/story1.story")
      story.content must be equalTo "storyContent"
    }

  }

  "Story#jBehaveStory" should {

    "return instance of the org.jbehave.core.model.Story" in {
      val story = new Story("myStory.story") {
        override def content = "MOCK"
      }
      story.jBehaveStory must beAnInstanceOf[org.jbehave.core.model.Story]
    }

  }

  "Story#json" should {

    val storyAsString = """Narrative:
In order to communicate effectively to the business some functionality
As a development team
I want to use Behaviour-Driven Development

Lifecycle:
Before:
Given a step that is executed before each scenario
After:
Given a step that is executed after each scenario

Scenario:  A scenario is a collection of executable steps of different type

Given step represents a precondition to an event
When step represents the occurrence of the event
Then step represents the outcome of the event

Scenario:  Another scenario exploring different combination of events

Given a [precondition]
When a negative event occurs
Then a the outcome should [be-captured]

Examples:
|precondition|be-captured|
|abc|be captured    |
|xyz|not be captured|"""

    val story = new Story("MOCK") {
      override def content = storyAsString
      override def name = "myStory"
    }

    "have name" in {
      story.json.toString must /("name" -> "myStory")
    }

    "have narrative" in {
      val json = story.json.toString
      json must /("narrative") */("inOrderTo" -> "communicate effectively to the business some functionality")
      json must /("narrative") */("asA" -> "development team")
      json must /("narrative") */("iWantTo" -> "use Behaviour-Driven Development")
    }

  }

  "Story object" should {

    "return Story instance when apply is called" in {
      Story("test/stories/story1.story") must beAnInstanceOf[Story]
    }

  }

}
