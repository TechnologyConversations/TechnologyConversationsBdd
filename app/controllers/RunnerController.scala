package controllers

import models.db.BddDb
import models.file.BddFile
import play.api.mvc.{AnyContent, Action, Controller}
import models.{Story, RunnerClass, Runner}
import play.api.libs.json.JsValue
import play.api.Play
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import util.Imports._
import scala.concurrent.Future

class RunnerController extends Controller {

  lazy val storiesDir = Play.current.configuration.getString("stories.root.dir").getOrElse("data/stories")
  lazy val runnerStoriesDir = Play.current.configuration.getString("stories.runner.dir").getOrElse("data/runner_stories")
  lazy val bddDb = if (mongoEnabled) Option(BddDb(mongoUri, mongoDb)) else Option.empty
  lazy val bddFile = Option(BddFile())
  lazy val story = Story(bddFile, bddDb)
  // TODO Remove
  val mongoDbIsEnabled = featureIsEnabled("mongoDb")

  def run: Action[AnyContent] = Action { implicit request =>
    val reportsId = System.currentTimeMillis()
    val json = request.body.asJson
    val resultMap = validate(json, reportsId)
    if (resultMap("status") == "OK") {
      if (bddDb.isDefined && mongoDbIsEnabled) {
        Future(runStoriesFromDb(json, reportsId))
      } else {
        Future(runStoriesFromFile(json, reportsId))
      }
    }
    result(resultMap)
  }

  private[controllers] def validate(jsonOption: Option[JsValue], reportsId: Long): Map[String, String] = {
    lazy val json = jsonOption.get
    lazy val storyPaths = (json \ "storyPaths").asOpt[List[JsValue]]
    lazy val classesJson = (json \ "classes").asOpt[List[JsValue]]
    if (jsonOption.isEmpty) {
      noJsonResultMap
    } else if (storyPaths.isEmpty) {
      noResultMap("storyPaths")
    } else if (classesJson.isEmpty || classesJson.get.size == 0) {
      noResultMap("classes")
    } else {
      Map(
        "status" -> "OK",
        "id" -> reportsId.toString,
        "reportsPath" -> s"$reportsId/view/reports.html"
      )
    }
  }

  private[controllers] def runStoriesFromDb(jsonOption: Option[JsValue], reportsId: Long) {
    val storiesDir = s"$runnerStoriesDir/$reportsId"
    story.storiesFromMongoDbToFiles(storiesDir)
    runStories(jsonOption, reportsId, storiesDir)
  }

  private[controllers] def runStoriesFromFile(jsonOption: Option[JsValue], reportsId: Long) {
    runStories(jsonOption, reportsId, storiesDir)
  }

  private[controllers] def runStories(jsonOption: Option[JsValue],
                                      reportsId: Long,
                                      storiesDir: String) {
    val json = jsonOption.get
    val storyPaths = (json \ "storyPaths").asOpt[List[JsValue]]
    val classesJson = (json \ "classes").asOpt[List[JsValue]]
    val compositesJsonOpt = (json \ "composites").asOpt[List[JsValue]]
    val groovyCompositesJsonOpt = (json \ "groovyComposites").asOpt[List[JsValue]]
    val fullStoryPaths = storyPaths.get.map { path =>
      storiesDir + "/" + (path \ "path").as[String]
    }
    val runner = new Runner(
      fullStoryPaths,
      classesFromSteps(classesJson.get) ::: classesFromComposites(compositesJsonOpt),
      groovyCompositesJsonOpt.getOrElse(List()).map(composite => (composite \ "path").as[String]),
      reportsRelativeDir + "/" + reportsId
    )
    try {
      runner.run()
    } finally {
      runner.cleanUp()
    }
  }

  // TODO Move to a separate class (without Controller) for easier testing
  private[controllers] def classesFromSteps(json: List[JsValue]) = {
    json.map { element =>
      val fullName = (element \ "fullName").as[String]
      val paramsJson = (element \ "params").asOpt[List[JsValue]]
      val params = paramsJson.getOrElse(List()).map { paramJson =>
        val key = (paramJson \ "key").as[String]
        val value = (paramJson \ "value").asOpt[String].getOrElse("")
        (key, value)
      }.toMap
      RunnerClass(fullName, params)
    }
  }

  // TODO Move to a separate class (without Controller) for easier testing
  private[controllers] def classesFromComposites(jsonOption: Option[List[JsValue]]) = {
    if (jsonOption.isEmpty) {
      List()
    } else {
      jsonOption.get.map {
        element =>
          val packageName = (element \ "package").as[String]
          val className = (element \ "class").as[String]
          RunnerClass(s"$packageName.$className")
      }
    }
  }

}

object RunnerController extends RunnerController