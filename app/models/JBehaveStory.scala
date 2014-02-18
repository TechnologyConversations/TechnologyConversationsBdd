package models

import org.jbehave.core.parsers.RegexStoryParser
import play.api.libs.json._
import scala.collection.JavaConversions._
import org.jbehave.core.model._
import java.util.Properties

trait JBehaveStory {

  def name: String

  def content: String

  def jBehaveStory = new RegexStoryParser().parseStory(content)

  def toJson: JsValue = Json.toJson(rootCollection)

  def fromJson(json: JsValue): org.jbehave.core.model.Story = {
    new org.jbehave.core.model.Story(
      fromJsonPath(json),
      fromJsonDescription(json),
      fromJsonMeta(json),
      fromJsonNarrative(json),
      fromJsonGivenStories(json),
      fromJsonLifecycle(json),
      null // java.util.List<org.jbehave.core.model.Scenario>
    )
  }

  def fromJsonPath(json: JsValue) = (json \ "name").as[String] + ".story"

  def fromJsonDescription(json: JsValue) = new Description((json \ "description").as[String])

  def fromJsonMeta(json: JsValue) = {
    val meta = (json \ "meta" \\ "element").foldLeft(new Properties) ((out, in) => {
      val keyValue = in.as[String].split(" ")
      out.put(keyValue.head, keyValue.tail.mkString(" "))
      out
    })
    new Meta(meta)
  }

  def fromJsonNarrative(json: JsValue) = {
    new Narrative(
      (json \ "narrative" \ "inOrderTo").as[String],
      (json \ "narrative" \ "asA").as[String],
      (json \ "narrative" \ "iWantTo").as[String]
    )
  }

  def fromJsonGivenStories(json: JsValue) = {
    new GivenStories(
      (json \ "givenStories" \\ "story").foldLeft(List[String]()) ((out, in) => {
        out :+ in.as[String]
      }).mkString(",")
    )
  }

  def fromJsonLifecycle(json: JsValue) = {
    new Lifecycle(
      (json \ "lifecycle" \ "before" \\ "step").map(_.as[String]),
      (json \ "lifecycle" \ "after" \\ "step").map(_.as[String])
    )
  }

  def fromJsonScenarios(json: JsValue) = {
    val scenarios = (json \ "scenarios").as[List[JsObject]]
    // scala> (json \ "root").as[List[JsObject]].map({ i => (i \ "val").as[Long] * (i \ "weight").as[Double] }).sum
    scenarios.map(scenarioJson =>
      new Scenario()
    )
//    "scenarios":
//    [
//    {
//      "title": "A scenario is a collection of executable steps of different type",
//      "meta": [ { "element": "live" }, { "element": "product shopping cart" } ],
//      "steps":
//      [
//      { "step": "Given step represents a precondition to an event" },
//      { "step": "When step represents the occurrence of the event" },
//      { "step": "Then step represents the outcome of the event" }
//      ],
//      "examplesTable": ""
//    },
  }

  def rootCollection = {
    Map(
      "name" -> Json.toJson(name),
      "description" -> Json.toJson(jBehaveStory.getDescription.asString),
      "meta" -> Json.toJson(metaCollection(jBehaveStory.getMeta)),
      "narrative" -> Json.toJson(narrativeCollection(jBehaveStory.getNarrative)),
      "givenStories" -> Json.toJson(givenStoriesCollection(jBehaveStory.getGivenStories.getPaths.toList)),
      "lifecycle" -> Json.toJson(lifecycleCollection(jBehaveStory.getLifecycle)),
      "scenarios" -> Json.toJson(scenariosCollection(jBehaveStory.getScenarios.toList))
    )
  }

  def metaCollection(meta: Meta) = {
    meta.getPropertyNames.map(name => Map("element" -> (name + " " + meta.getProperty(name)).trim))
  }

  def givenStoriesCollection(givenStories: List[String]) = {
    givenStories.map(story => Map("story" -> story))
  }

  def narrativeCollection(narrative: Narrative) = {
    Map(
      "inOrderTo" -> narrative.inOrderTo,
      "asA" -> narrative.asA,
      "iWantTo" -> narrative.iWantTo
    )
  }

  def lifecycleCollection(lifecycle: Lifecycle) = {
    Map(
      "before" -> stepsCollection(lifecycle.getBeforeSteps.toList),
      "after" -> stepsCollection(lifecycle.getAfterSteps.toList)
    )
  }

  def scenariosCollection(scenarios: List[Scenario]) = {
    scenarios.map(scenario =>
      Map(
        "title" -> Json.toJson(scenario.getTitle),
        "meta" -> Json.toJson(metaCollection(scenario.getMeta)),
        "steps" -> Json.toJson(stepsCollection(scenario.getSteps.toList)),
        "examplesTable" -> Json.toJson(scenario.getExamplesTable.asString)
      )
    )
  }

  def stepsCollection(steps: List[String]) = {
    steps.map(step => Map("step" -> step))
  }

}
