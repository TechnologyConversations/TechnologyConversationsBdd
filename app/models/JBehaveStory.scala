package models

import org.jbehave.core.parsers.RegexStoryParser
import play.api.libs.json._
import scala.collection.JavaConversions._
import org.jbehave.core.model._
import java.util.Properties

trait JBehaveStory {

  def name: String

  def content: String

  def toText(json: JsValue): String = {
    val story = toJBehaveStory(json)
    val beforeScenarios = toTextBeforeScenarios(story)
    val scenarios = toTextScenarios(story)
    s"""
$beforeScenarios

$scenarios
""".toString
  }

  def toJson: JsValue = Json.toJson(rootCollection)

  private[models] def toTextBeforeScenarios(story: org.jbehave.core.model.Story) = {
    val description = story.getDescription.asString
    val meta = toTextMeta(story.getMeta)
    val narrativeInOrderTo = story.getNarrative.inOrderTo
    val narrativeAsA = story.getNarrative.asA
    val narrativeIWantTo = story.getNarrative.iWantTo
    val givenStories = story.getGivenStories.getPaths.mkString(", ")
    val lifecycleBefore = story.getLifecycle.getBeforeSteps.mkString("\n")
    val lifecycleAfter = story.getLifecycle.getAfterSteps.mkString("\n")
    s"""$description
       |
       |Meta:
       |$meta
       |
       |Narrative:
       |In order to $narrativeInOrderTo
       |As a $narrativeAsA
       |I want to $narrativeIWantTo
       |
       |GivenStories: $givenStories
       |
       |Lifecycle:
       |
       |Before:
       |$lifecycleBefore
       |
       |After:
       |$lifecycleAfter
       |""".stripMargin
  }

  private[models] def toTextMeta(meta: Meta) = {
    meta.getPropertyNames.map(
      name => ("@" + name + " " + meta.getProperty(name)).trim
    ).mkString("\n")
  }

  private[models] def toTextScenarios(story: org.jbehave.core.model.Story) = {
    story.getScenarios.map(scenario => {
      val title = scenario.getTitle
      val meta = toTextMeta(scenario.getMeta)
      val steps = scenario.getSteps.mkString("\n")
      val examples = scenario.getExamplesTable.asString
      s"""
Scenario: $title

Meta:
$meta

$steps

Examples:
$examples
      """.trim
    }).mkString("\n\n")
  }

  private[models] def toJBehaveStory(json: JsValue): org.jbehave.core.model.Story = {
    new org.jbehave.core.model.Story(
      fromJsonPath(json),
      fromJsonDescription(json),
      fromJsonMeta(json),
      fromJsonNarrative(json),
      fromJsonGivenStories(json),
      fromJsonLifecycle(json),
      fromJsonScenarios(json)
    )
  }

  private[models] def fromJsonPath(json: JsValue) = (json \ "name").as[String] + ".story"

  private[models] def fromJsonDescription(json: JsValue) = new Description((json \ "description").as[String])

  private[models] def fromJsonMeta(json: JsValue) = {
    val meta = (json \ "meta" \\ "element").foldLeft(new Properties) ((out, in) => {
      val keyValue = in.as[String].split(" ")
      out.put(keyValue.head, keyValue.tail.mkString(" "))
      out
    })
    new Meta(meta)
  }

  private[models] def fromJsonNarrative(json: JsValue) = {
    new Narrative(
      (json \ "narrative" \ "inOrderTo").as[String],
      (json \ "narrative" \ "asA").as[String],
      (json \ "narrative" \ "iWantTo").as[String]
    )
  }

  private[models] def fromJsonGivenStories(json: JsValue) = {
    new GivenStories(
      (json \ "givenStories" \\ "story").foldLeft(List[String]()) ((out, in) => {
        out :+ in.as[String]
      }).mkString(",")
    )
  }

  private[models] def fromJsonLifecycle(json: JsValue) = {
    new Lifecycle(
      (json \ "lifecycle" \ "before" \\ "step").map(_.as[String]),
      (json \ "lifecycle" \ "after" \\ "step").map(_.as[String])
    )
  }

  private[models] def fromJsonScenarios(json: JsValue) = {
    val scenarios = (json \ "scenarios").as[List[JsObject]]
    // scala> (json \ "root").as[List[JsObject]].map({ i => (i \ "val").as[Long] * (i \ "weight").as[Double] }).sum
    scenarios.map(scenarioJson =>
      new Scenario(
        (scenarioJson \ "title").as[String],
        fromJsonMeta(scenarioJson),
        GivenStories.EMPTY,
        new ExamplesTable((scenarioJson \ "examplesTable").as[String]),
        (scenarioJson \ "steps" \\ "step").map(_.as[String])
      )
    )

  }

  private[models] def parseStory(content: String) = new RegexStoryParser().parseStory(content)

  private[models] def rootCollection = {
    val story = parseStory(content)
    Map(
      "name" -> Json.toJson(name),
      "description" -> Json.toJson(story.getDescription.asString),
      "meta" -> Json.toJson(metaCollection(story.getMeta)),
      "narrative" -> Json.toJson(narrativeCollection(story.getNarrative)),
      "givenStories" -> Json.toJson(givenStoriesCollection(story.getGivenStories.getPaths.toList)),
      "lifecycle" -> Json.toJson(lifecycleCollection(story.getLifecycle)),
      "scenarios" -> Json.toJson(scenariosCollection(story.getScenarios.toList))
    )
  }

  private[models] def metaCollection(meta: Meta) = {
    meta.getPropertyNames.map(name => Map("element" -> (name + " " + meta.getProperty(name)).trim))
  }

  private[models] def givenStoriesCollection(givenStories: List[String]) = {
    givenStories.map(story => Map("story" -> story))
  }

  private[models] def narrativeCollection(narrative: Narrative) = {
    Map(
      "inOrderTo" -> narrative.inOrderTo,
      "asA" -> narrative.asA,
      "iWantTo" -> narrative.iWantTo
    )
  }

  private[models] def lifecycleCollection(lifecycle: Lifecycle) = {
    Map(
      "before" -> stepsCollection(lifecycle.getBeforeSteps.toList),
      "after" -> stepsCollection(lifecycle.getAfterSteps.toList)
    )
  }

  private[models] def scenariosCollection(scenarios: List[Scenario]) = {
    scenarios.map(scenario =>
      Map(
        "title" -> Json.toJson(scenario.getTitle),
        "meta" -> Json.toJson(metaCollection(scenario.getMeta)),
        "steps" -> Json.toJson(stepsCollection(scenario.getSteps.toList)),
        "examplesTable" -> Json.toJson(scenario.getExamplesTable.asString)
      )
    )
  }

  private[models] def stepsCollection(steps: List[String]) = {
    steps.map(step => Map("step" -> step))
  }

}
