package models

import java.util.Properties
import org.jbehave.core.model._
import play.api.libs.json.Json
import org.specs2.mutable.Specification
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

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

    val rootCollection = JBehaveStoryMock.rootCollection

    "have name" in {
      rootCollection must havePair("name" -> Json.toJson("myStory"))
    }

    "have path" in {
      rootCollection must havePair("path" -> Json.toJson(JBehaveStoryMock.path))
    }

    "have description" in {
      rootCollection must havePair("description" -> Json.toJson("This is description of this story"))
    }

    "have meta" in {
      val properties = new Properties
      properties.put("integration", "")
      properties.put("product", "dashboard")
      val meta = new Meta(properties)
      rootCollection must havePair("meta" -> Json.toJson(JBehaveStoryMock.metaCollection(meta)))
    }

    "have givenStories" in {
      val givenStories = List("story1.story", "story2.story", "story3.story")
      rootCollection must havePair("givenStories" -> Json.toJson(JBehaveStoryMock.givenStoriesCollection(givenStories)))
    }

    "have narrative" in {
      val narrative = new Narrative(
        "communicate effectively to the business some functionality",
        "development team",
        "use Behaviour-Driven Development"
      )
      rootCollection must havePair("narrative" -> Json.toJson(JBehaveStoryMock.narrativeCollection(narrative)))
    }

    "have lifecycle" in {
      val lifecycle = new Lifecycle(
        ListBuffer("Given a step that is executed before each scenario").asJava,
        ListBuffer("Given a step that is executed after each scenario").asJava
      )
      rootCollection must havePair("lifecycle" -> Json.toJson(JBehaveStoryMock.lifecycleCollection(lifecycle)))
    }

    "have scenarios" in {
      val title = "Another scenario exploring different combination of events"
      val steps = ListBuffer(
        "Given a precondition",
        "When a negative event occurs",
        "Then a the outcome should be-captured"
      ).asJava
      val scenarios = List(new Scenario(title, steps))
      rootCollection must havePair("scenarios" -> Json.toJson(JBehaveStoryMock.scenariosCollection(scenarios)))
    }

  }

  "JBehaveStory#toJson" should {

    "return json representation of rootCollection" in {
      JBehaveStoryMock.toJson must be equalTo Json.toJson(JBehaveStoryMock.rootCollection)
    }

  }

  "JBehaveStory#fromJsonPath" should {

    "return story path" in {
      JBehaveStoryMock.fromJsonPath(mockJson) must be equalTo "story1.story"
    }

  }

  "JBehaveStory#fromJsonDescription" should {

    "return story description" in {
      JBehaveStoryMock.fromJsonDescription(mockJson).asString must be equalTo "This is description of this story"
    }

  }

  "JBehaveStory#fromJsonMeta" should {

    "return story meta" in {
      val meta = JBehaveStoryMock.fromJsonMeta(mockJson)
      meta.getPropertyNames must have size 2
      meta.getProperty("integration") must be equalTo ""
      meta.getProperty("product") must be equalTo "dashboard"
    }

  }

  "JBehaveStory#fromJsonNarrative" should {

    "return story narrative" in {
      val narrative = JBehaveStoryMock.fromJsonNarrative(mockJson)
      narrative.inOrderTo must be equalTo "communicate effectively to the business some functionality"
      narrative.asA must be equalTo "development team"
      narrative.iWantTo must be equalTo "use Behaviour-Driven Development"
    }

  }

  "JBehaveStory#fromJsonGivenStories" should {

    "return story narrative" in {
      val paths = JBehaveStoryMock.fromJsonGivenStories(mockJson).getPaths.toList
      paths must have size 3
      paths must containTheSameElementsAs(Seq("story1.story","story2.story", "story3.story"))
    }

  }

  "JBehaveStory#fromJsonLifecycle" should {

    "return story narrative" in {
      val lifecycle = JBehaveStoryMock.fromJsonLifecycle(mockJson)
      val beforeSteps = lifecycle.getBeforeSteps.toList
      val afterSteps = lifecycle.getAfterSteps.toList
      beforeSteps must have size 2
      afterSteps must have size 1
      beforeSteps must containTheSameElementsAs(Seq(
        "Given a step that is executed before each scenario",
        "Given another step that is executed before each scenario"
      ))
      afterSteps must containTheSameElementsAs(Seq(
        "Given a step that is executed after each scenario"
      ))
    }

  }

  "JBehaveStory#fromJsonScenarios" should {

    val scenarios = JBehaveStoryMock.fromJsonScenarios(mockJson)
    val scenario = scenarios(0)

    "return story scenarios" in {
      val scenarios = JBehaveStoryMock.fromJsonScenarios(mockJson)
      scenarios must have size 2
    }

    "return story scenarios with title" in {
      scenario.getTitle must be equalTo "A scenario is a collection of executable steps of different type"
    }

    "return story scenarios with meta" in {
      val meta = scenario.getMeta
      meta.getPropertyNames must have size 2
      meta.getProperty("live") must be equalTo ""
      meta.getProperty("product") must be equalTo "shopping cart"
    }

    "return story scenarios with examples table" in {
      scenario.getExamplesTable.asString.trim must be equalTo "|precondition|be-captured|\n|abc|be captured|\n|xyz|not be captured|"
    }

    "return story steps" in {
      val expected = List(
        "Given step represents a precondition to an event",
        "When step represents the occurrence of the event",
        "Then step represents the outcome of the event"
      )
      val steps = scenario.getSteps.toList
      steps must have size 3
      steps must containTheSameElementsAs(expected)
    }

  }

  "JBehaveStory#toJBehaveStory" should {

    val jBehaveStory = JBehaveStoryMock.toJBehaveStory(mockJson)

    "return org.jbehave.core.model.Story object" in {
      jBehaveStory must beAnInstanceOf[org.jbehave.core.model.Story]
    }

    "return org.jbehave.core.model.Story object with path" in {
      jBehaveStory.getPath must be equalTo JBehaveStoryMock.fromJsonPath(mockJson)
    }

    "return org.jbehave.core.model.Story object with description" in {
      jBehaveStory.getDescription must beAnInstanceOf[org.jbehave.core.model.Description]
    }

    "return org.jbehave.core.model.Story object with meta" in {
      jBehaveStory.getMeta must beAnInstanceOf[org.jbehave.core.model.Meta]
    }

    "return org.jbehave.core.model.Story object with narrative" in {
      jBehaveStory.getNarrative must beAnInstanceOf[org.jbehave.core.model.Narrative]
    }

    "return org.jbehave.core.model.Story object with given stories" in {
      jBehaveStory.getGivenStories must beAnInstanceOf[org.jbehave.core.model.GivenStories]
    }

    "return org.jbehave.core.model.Story object with lifecycle" in {
      jBehaveStory.getLifecycle must beAnInstanceOf[org.jbehave.core.model.Lifecycle]
    }

    "return org.jbehave.core.model.Story object with scenarios" in {
      val scenarios = jBehaveStory.getScenarios
      scenarios must have size 2
      scenarios(0) must beAnInstanceOf[org.jbehave.core.model.Scenario]
    }

  }

  "JBehaveStory#toText" should {

    val text = JBehaveStoryMock.toText(mockJson)

    "contain description" in {
      text must contain("This is description of this story")
    }

    "contain meta" in {
      text must contain("Meta:")
      text must contain("@integration")
      text must contain("@product dashboard")
    }

    "contain narrative" in {
      val expected = """Narrative:
                       |In order to communicate effectively to the business some functionality
                       |As a development team
                       |I want to use Behaviour-Driven Development""".stripMargin
      text must contain(expected)
    }

    "contain given stories" in {
      text must contain("GivenStories: story1.story, story2.story, story3.story")
    }

    "contain lifecycle" in {
      text must contain("Lifecycle:")
      text must contain("Before:")
      text must contain("Given a step that is executed before each scenario")
      text must contain("After:")
      text must contain("Given a step that is executed after each scenario")
    }

    "contain scenarios" in {
      text must contain("Scenario: A scenario is a collection of executable steps of different type")
      text must contain("Meta:")
      text must contain("@live")
      text must contain("@product shopping cart")
      text must contain("Given step represents a precondition to an event")
      text must contain("When step represents the occurrence of the event")
      text must contain("Then step represents the outcome of the event")
      text must contain("Examples:")
      text must contain("|precondition|be-captured|")
      text must contain("|abc|be captured|")
      text must contain("|xyz|not be captured|")
    }

    "must be valid" in {
      val story = JBehaveStoryMock.parseStory(text)
      story.getDescription.asString must be equalTo "This is description of this story"
      story.getMeta.getPropertyNames must have size 2
      story.getNarrative.asA must be equalTo "development team"
      story.getGivenStories.getPaths must have size 3
      story.getLifecycle.getBeforeSteps must have size 2
      story.getLifecycle.getAfterSteps must have size 1
      story.getScenarios must have size 2
      val scenario = story.getScenarios.toList(0)
      scenario.getTitle must be equalTo "A scenario is a collection of executable steps of different type"
      scenario.getMeta.getPropertyNames must have size 2
      scenario.getSteps must have size 3
      scenario.getExamplesTable.asString must beEqualTo("|precondition|be-captured|\n|abc|be captured|\n|xyz|not be captured|").trimmed
      val scenarioReduced = story.getScenarios.toList(1)
      scenarioReduced.getMeta.getPropertyNames must have size 0
      scenarioReduced.getExamplesTable.asString must be equalTo ""
    }

  }

  val mockJsonString =
    """
{
  "name": "story1",
  "description": "This is description of this story",
  "meta": [ { "element": "integration" }, { "element": "product dashboard" } ],
  "narrative":
  {
    "inOrderTo": "communicate effectively to the business some functionality",
    "asA": "development team",
    "iWantTo": "use Behaviour-Driven Development"
  },
  "givenStories":
  [
    { "story": "story1.story" },
    { "story": "story2.story" },
    { "story": "story3.story" }
  ],
  "lifecycle":
  {
    "before":
    [
      { "step": "Given a step that is executed before each scenario" },
      { "step": "Given another step that is executed before each scenario" }
    ],
    "after":
    [
      { "step": "Given a step that is executed after each scenario" }
    ]
  },
  "scenarios":
  [
    {
      "title": "A scenario is a collection of executable steps of different type",
      "meta": [ { "element": "live" }, { "element": "product shopping cart" } ],
      "steps":
      [
        { "step": "Given step represents a precondition to an event" },
        { "step": "When step represents the occurrence of the event" },
        { "step": "Then step represents the outcome of the event" }
      ],
      "examplesTable": "|precondition|be-captured|\n|abc|be captured|\n|xyz|not be captured|"
    },
    {
      "title": "Another scenario exploring different combination of events",
      "meta": [],
      "steps":
      [
        { "step": "Given a [precondition]" },
        { "step": "When a negative event occurs" },
        { "step": "Then a the outcome should [be-captured]" }
      ],
      "examplesTable": ""
    }
  ]
}""".stripMargin
  val mockJson = Json.parse(mockJsonString)

}

object JBehaveStoryMock extends JBehaveStory {

  override val path = "myDir/myStory.story"
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