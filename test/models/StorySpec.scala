package models

import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
import play.api.libs.json.Json
import scala.util.parsing.json.JSONArray

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

  "Story#json" should {

    val storyAsString = """
This is description of this story

Meta:
@integration
@product dashboard

Narrative:
In order to communicate effectively to the business some functionality
As a development team
I want to use Behaviour-Driven Development

GivenStories: story1.story, story2.story, story3.story

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
When step represents the occurrence of the event
Then step represents the outcome of the event

Scenario: Another scenario exploring different combination of events

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

    val jsonString = story.json.toString

    "have name" in {
      jsonString must /("name" -> "myStory")
    }

    "have description" in {
      jsonString must /("description" -> "This is description of this story")
    }

    "have narrative" in {
      jsonString must /("narrative") /("inOrderTo" -> "communicate effectively to the business some functionality")
      jsonString must /("narrative") /("asA" -> "development team")
      jsonString must /("narrative") /("iWantTo" -> "use Behaviour-Driven Development")
    }

    "have lifecycle" in {
      val beforeJson = JSONArray(List("Given a step that is executed before each scenario"))
      val afterJson = JSONArray(List("Given a step that is executed after each scenario"))
      jsonString must /("lifecycle") /("before" -> beforeJson)
      jsonString must /("lifecycle") /("after" -> afterJson)
    }

    "have givenStories" in {
      val givenStoriesJson = JSONArray(List("story1.story", "story2.story", "story3.story"))
      jsonString must /("givenStories" -> givenStoriesJson)
    }

    "have meta tags" in {
      val metaJson = JSONArray(List("integration", "product dashboard"))
      jsonString must /("meta" -> metaJson)
    }

    "have scenario title" in {
      jsonString must /("scenarios") */("title" -> "A scenario is a collection of executable steps of different type")
      jsonString must /("scenarios") */("title" -> "Another scenario exploring different combination of events")
    }

//    TODO Figure out how to test scenario metas, steps and examples
//    "have scenario metas" in {
//      val metaJson = JSONArray(List("product shopping cart", "live"))
//      jsonString must /("scenarios") */("meta" -> metaJson)
//    }

  }

  "Story object" should {

    "return Story instance when apply is called" in {
      Story("test/stories/story1.story") must beAnInstanceOf[Story]
    }

  }

}