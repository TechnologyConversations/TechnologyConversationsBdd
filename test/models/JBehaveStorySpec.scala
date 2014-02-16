package models

import java.util.Properties
import org.jbehave.core.model._
import play.api.libs.json.Json
import org.specs2.mutable.Specification
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._

class JBehaveStorySpec extends Specification {

  "JBehaveStory#stepCollection" should {

    val steps = List("Given condition", "When action", "Then validation")

    "return list of all steps" in {
      JBehaveStoryMock.stepsCollection(steps) must have size 3
    }

    "have step -> value pairs" in {
      val expected = Seq(
        Map("step" -> "Given condition"),
        Map("step" -> "When action"),
        Map("step" -> "Then validation")
      )
      JBehaveStoryMock.stepsCollection(steps) must containTheSameElementsAs(expected)
    }

  }

  "JBehaveStory#name" should {

    "return story name without extension" in {
      JBehaveStoryMock.name must be equalTo "myStory"
    }

  }

  "JBehaveStory#metaCollection" should {

    val properties = new Properties
    properties.put("key1", "value1")
    properties.put("key2", "")
    val meta = new Meta(properties)

    "have correct size" in {
      JBehaveStoryMock.metaCollection(meta) must have size 2
    }

    "have map sequence (element -> value)" in {
      val elements = Seq(Map("element" -> "key1 value1"), Map("element" -> "key2"))
      JBehaveStoryMock.metaCollection(meta) must containTheSameElementsAs(elements)
    }

  }

  "JBehaveStory#givenStoriesCollection" should {

    val givenStories = List("story1.story", "story2.story", "story3.story")

    "have correct size" in {
      JBehaveStoryMock.givenStoriesCollection(givenStories) must have size 3
    }

    "have map sequence (story -> value)" in {
      val elements = Seq(
        Map("story" -> "story1.story"),
        Map("story" -> "story2.story"),
        Map("story" -> "story3.story"))
      JBehaveStoryMock.givenStoriesCollection(givenStories) must containTheSameElementsAs(elements)
    }

  }

  "JBehaveStory#narrativeCollection" should {

    val narrative = new Narrative("accomplish something", "someone", "do something")

    "have size 3" in {
      JBehaveStoryMock.narrativeCollection(narrative) must have size 3
    }

    "have inOrderTo key" in {
      JBehaveStoryMock.narrativeCollection(narrative) must havePair("inOrderTo" -> "accomplish something" )
    }

    "have asA key" in {
      JBehaveStoryMock.narrativeCollection(narrative) must havePair("asA" -> "someone" )
    }

    "have iWantTo key" in {
      JBehaveStoryMock.narrativeCollection(narrative) must havePair("iWantTo" -> "do something" )
    }

  }

  "JBehaveStory#lifecycleCollection" should {

    val lifecycle = new Lifecycle(
      ListBuffer("When before step1", "Then before outcome1").asJava,
      ListBuffer("When after step1").asJava
    )

    "have size 2" in {
      JBehaveStoryMock.lifecycleCollection(lifecycle) must have size 2
    }

    "have before key" in {
      JBehaveStoryMock.lifecycleCollection(lifecycle) must haveKey("before")
    }

    "have before steps" in {
      val before = JBehaveStoryMock.lifecycleCollection(lifecycle)("before")
      before must have size 2
      val steps = JBehaveStoryMock.stepsCollection(List("When before step1", "Then before outcome1"))
      before must containTheSameElementsAs(steps)
    }

    "have after key" in {
      JBehaveStoryMock.lifecycleCollection(lifecycle) must haveKey("after")
    }

    "have after steps" in {
      val after = JBehaveStoryMock.lifecycleCollection(lifecycle)("after")
      after must have size 1
      val steps = JBehaveStoryMock.stepsCollection(List("When after step1"))
      after must containTheSameElementsAs(steps)
    }

  }

  "JBehaveStory#scenariosCollection" should {

    val properties = new Properties
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
    val actualScenario =  JBehaveStoryMock.scenariosCollection(List(scenario))(0)

    "have all scenarios" in {
      JBehaveStoryMock.scenariosCollection(List(scenario, scenario, scenario)) must have size 3
    }

    "have title" in {
      actualScenario must havePair("title" -> Json.toJson("myTitle"))
    }

    "have meta" in {
      actualScenario must havePair("meta" -> Json.toJson(JBehaveStoryMock.metaCollection(meta)))
    }

    "have all steps" in {
      actualScenario must havePair("steps" -> Json.toJson(JBehaveStoryMock.stepsCollection(steps)))
    }

    "have examples table" in {
      actualScenario must havePair("examplesTable" -> Json.toJson(examplesTable))
    }

  }

  "JBehaveStory#rootCollection" should {

    "have name" in {
      JBehaveStoryMock.rootCollection must havePair("name" -> Json.toJson("myStory"))
    }

    "have description" in {
      JBehaveStoryMock.rootCollection must havePair("description" -> Json.toJson("This is description of this story"))
    }

    "have meta" in {
      val properties = new Properties
      properties.put("integration", "")
      properties.put("product", "dashboard")
      val meta = new Meta(properties)
      JBehaveStoryMock.rootCollection must havePair("meta" -> Json.toJson(JBehaveStoryMock.metaCollection(meta)))
    }

    "have givenStories" in {
      val givenStories = List("story1.story", "story2.story", "story3.story")
      JBehaveStoryMock.rootCollection must havePair("givenStories" -> Json.toJson(JBehaveStoryMock.givenStoriesCollection(givenStories)))
    }

    "have narrative" in {
      val narrative = new Narrative(
        "communicate effectively to the business some functionality",
        "development team",
        "use Behaviour-Driven Development"
      )
      JBehaveStoryMock.rootCollection must havePair("narrative" -> Json.toJson(JBehaveStoryMock.narrativeCollection(narrative)))
    }

    "have lifecycle" in {
      val lifecycle = new Lifecycle(
        ListBuffer("Given a step that is executed before each scenario").asJava,
        ListBuffer("Given a step that is executed after each scenario").asJava
      )
      JBehaveStoryMock.rootCollection must havePair("lifecycle" -> Json.toJson(JBehaveStoryMock.lifecycleCollection(lifecycle)))
    }

    "have scenarios" in {
      val title = "Another scenario exploring different combination of events"
      val steps = ListBuffer(
        "Given a precondition",
        "When a negative event occurs",
        "Then a the outcome should be-captured"
      ).asJava
      val scenarios = List(new Scenario(title, steps))
      JBehaveStoryMock.rootCollection must havePair("scenarios" -> Json.toJson(JBehaveStoryMock.scenariosCollection(scenarios)))
    }

  }

  "JBehaveStory#json" should {

    "return json representation of rootCollection" in {
      JBehaveStoryMock.json must be equalTo Json.toJson(JBehaveStoryMock.rootCollection)
    }

  }

}

object JBehaveStoryMock extends JBehaveStory {

  override def content = """
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

  override def name = "myStory"

}