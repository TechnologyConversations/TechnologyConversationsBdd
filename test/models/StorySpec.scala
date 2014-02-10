package models

import org.specs2.mutable.Specification
import org.specs2.matcher.JsonMatchers
import org.jbehave.core.model._
import java.util.Properties
import scala.Predef._
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import play.api.libs.json.Json

class StorySpec extends Specification with JsonMatchers {

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

Scenario: Another scenario exploring different combination of events

Given a precondition
When a negative event occurs
Then a the outcome should be-captured
"""

  val mockStory = new Story("MOCK") {
    override def content = storyAsString
    override def name = "myStory"
  }

  "Story#stepCollection" should {

    val story = new Story("myStory.story")
    val steps = List("Given condition", "When action", "Then validation")

    "return list of all steps" in {
      story.stepsCollection(steps) must have size 3
    }

    "have step -> value pairs" in {
      val expected = Seq(
        Map("step" -> "Given condition"),
        Map("step" -> "When action"),
        Map("step" -> "Then validation")
      )
      story.stepsCollection(steps) must containTheSameElementsAs(expected)
    }

  }

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

  "Story#metaCollection" should {

    val story = new Story("test/stories/story1.story")
    val properties = new Properties();
    properties.put("key1", "value1")
    properties.put("key2", "")
    val meta = new Meta(properties)

    "have correct size" in {
      story.metaCollection(meta) must have size 2
    }

    "have map sequence (element -> value)" in {
      val elements = Seq(Map("element" -> "key1 value1"), Map("element" -> "key2"))
      story.metaCollection(meta) must containTheSameElementsAs(elements)
    }

  }

  "Story#givenStoriesCollection" should {

    val story = new Story("test/stories/story1.story")
    val givenStories = List("story1.story", "story2.story", "story3.story")

    "have correct size" in {
      story.givenStoriesCollection(givenStories) must have size 3
    }

    "have map sequence (story -> value)" in {
      val elements = Seq(
        Map("story" -> "story1.story"),
        Map("story" -> "story2.story"),
        Map("story" -> "story3.story"))
      story.givenStoriesCollection(givenStories) must containTheSameElementsAs(elements)
    }

  }

  "Story#narrativeCollection" should {

    val story = new Story("test/stories/story1.story")
    val narrative = new Narrative("accomplish something", "someone", "do something")

    "have size 3" in {
      story.narrativeCollection(narrative) must have size 3
    }

    "have inOrderTo key" in {
      story.narrativeCollection(narrative) must havePair("inOrderTo" -> "accomplish something" )
    }

    "have asA key" in {
      story.narrativeCollection(narrative) must havePair("asA" -> "someone" )
    }

    "have iWantTo key" in {
      story.narrativeCollection(narrative) must havePair("iWantTo" -> "do something" )
    }

  }

  "Story#lifecycleCollection" should {

    val story = new Story("test/stories/story1.story")
    val lifecycle = new Lifecycle(
      ListBuffer("When before step1", "Then before outcome1").asJava,
      ListBuffer("When after step1").asJava
    )

    "have size 2" in {
      story.lifecycleCollection(lifecycle) must have size 2
    }

    "have before key" in {
      story.lifecycleCollection(lifecycle) must haveKey("before")
    }

    "have before steps" in {
      val before = story.lifecycleCollection(lifecycle)("before")
      before must have size 2
      val steps = story.stepsCollection(List("When before step1", "Then before outcome1"))
      before must containTheSameElementsAs(steps)
    }

    "have after key" in {
      story.lifecycleCollection(lifecycle) must haveKey("after")
    }

    "have after steps" in {
      val after = story.lifecycleCollection(lifecycle)("after")
      after must have size 1
      val steps = story.stepsCollection(List("When after step1"))
      after must containTheSameElementsAs(steps)
    }

  }

  "Story#scenariosCollection" should {

    val story = new Story("test/stories/story1.story")
    val properties = new Properties();
    properties.put("key1", "value1")
    properties.put("key2", "")
    val meta = new Meta(properties)
    val steps = List("Given condition", "When action", "Then validation")
    val examplesTable = "|precondition|be-captured|\n|abc|be captured|\n|xyz|not be captured|\n"
    val scenario = new Scenario(
      "myTitle",
      meta,
      new GivenStories(""),
      new ExamplesTable(examplesTable),
      steps.asJava
    )
    val actualScenario =  story.scenariosCollection(List(scenario))(0)

    "have all scenarios" in {
      story.scenariosCollection(List(scenario, scenario, scenario)) must have size 3
    }

    "have title" in {
      actualScenario must havePair("title" -> Json.toJson("myTitle"))
    }

    "have meta" in {
      actualScenario must havePair("meta" -> Json.toJson(story.metaCollection(meta)))
    }

    "have all steps" in {
      actualScenario must havePair("steps" -> Json.toJson(story.stepsCollection(steps)))
    }

    "have examples table" in {
      actualScenario must havePair("examplesTable" -> Json.toJson(examplesTable))
    }

  }

  "Story#rootCollection" should {

    "have name" in {
      mockStory.rootCollection must havePair("name" -> Json.toJson("myStory"))
    }

    "have description" in {
      mockStory.rootCollection must havePair("description" -> Json.toJson("This is description of this story"))
    }

    "have meta" in {
      val properties = new Properties();
      properties.put("integration", "")
      properties.put("product", "dashboard")
      val meta = new Meta(properties)
      mockStory.rootCollection must havePair("meta" -> Json.toJson(mockStory.metaCollection(meta)))
    }

    "have givenStories" in {
      val givenStories = List("story1.story", "story2.story", "story3.story")
      mockStory.rootCollection must havePair("givenStories" -> Json.toJson(mockStory.givenStoriesCollection(givenStories)))
    }

    "have narrative" in {
      val narrative = new Narrative(
        "communicate effectively to the business some functionality",
        "development team",
        "use Behaviour-Driven Development"
      )
      mockStory.rootCollection must havePair("narrative" -> Json.toJson(mockStory.narrativeCollection(narrative)))
    }

    "have lifecycle" in {
      val lifecycle = new Lifecycle(
        ListBuffer("Given a step that is executed before each scenario").asJava,
        ListBuffer("Given a step that is executed after each scenario").asJava
      )
      mockStory.rootCollection must havePair("lifecycle" -> Json.toJson(mockStory.lifecycleCollection(lifecycle)))
    }

    "have scenarios" in {
      val title = "Another scenario exploring different combination of events"
      val steps = ListBuffer(
        "Given a precondition",
        "When a negative event occurs",
        "Then a the outcome should be-captured"
      ).asJava
      val scenarios = List(new Scenario(title, steps))
      mockStory.rootCollection must havePair("scenarios" -> Json.toJson(mockStory.scenariosCollection(scenarios)))
    }

    "have empty name when path is an empty string" in {
      new Story("").rootCollection must havePair("name" -> Json.toJson(""))
    }

    "have empty description when path is an empty string" in {
      new Story("").rootCollection must havePair("description" -> Json.toJson(""))
    }

    "have empty meta when path is an empty string" in {
      new Story("").rootCollection must havePair("meta" -> Json.arr())
    }

    "have empty givenStories when path is an empty string" in {
      new Story("").rootCollection must havePair("givenStories" -> Json.arr())
    }

    "have narrative with empty inOrderTo, asA and iWantTo when path is an empty string" in {
      val story = new Story("")
      val narrative = new Narrative("", "", "")
      story.rootCollection must havePair("narrative" -> Json.toJson(story.narrativeCollection(narrative)))
    }

    "have lifecycle with empty before and after when path is an empty string" in {
      val story = new Story("")
      story.rootCollection must havePair("lifecycle" -> Json.toJson(story.lifecycleCollection(new Lifecycle())))
    }

    "have empty scenarios when path is an empty string" in {
      new Story("").rootCollection must havePair("scenarios" -> Json.arr())
    }

  }
  
  "Story#json" should {

    "return json representation of rootCollection" in {
      mockStory.json must be equalTo(Json.toJson(mockStory.rootCollection))
    }

  }

  "Story object" should {

    "return Story instance when apply is called" in {
      Story("test/stories/story1.story") must beAnInstanceOf[Story]
    }

  }

}