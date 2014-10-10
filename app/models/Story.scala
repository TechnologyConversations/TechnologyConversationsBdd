package models

import models.db.BddDb
import models.jbehave.JBehaveStory
import models.file.BddFile
import java.io.File
import play.api.libs.json.{JsValue, Json}
import util.Imports._


// TODO Move methods from extended classes to objects set as constructor arguments
// TODO Remove dir and path
class Story(val dir: String = "",
            val path: String = "",
            val bddFile: Option[BddFile] = Option.empty,
            val bddDb: Option[BddDb] = Option.empty)
  extends JBehaveStory {

  // TODO Remove
  val mongoDbIsEnabled = featureIsEnabled("mongoDb")

  def saveStory(file: File, json: JsValue, overwrite: Boolean): Boolean = {
    var dbSuccess = true
    var fileSuccess = true
    if (bddDb.isDefined && mongoDbIsEnabled) {
      dbSuccess = bddDb.get.upsertStory(json)
    }
    if (bddFile.isDefined) {
      fileSuccess = bddFile.get.saveFile(file, toText(json), overwrite)
    }
    dbSuccess && fileSuccess
  }

  def removeStory(file: File, storyPath: String): Boolean = {
    var dbSuccess = false
    var fileDeleted = false
    if (bddDb.isDefined && mongoDbIsEnabled) {
      dbSuccess = bddDb.get.removeStory(storyPath)
    }
    if (bddFile.isDefined) {
      fileDeleted = bddFile.get.deleteFile(file)
    }
    dbSuccess && fileDeleted
  }

  def findStory(file: File, storyPath: String): Option[JsValue] = {
    var story: Option[JsValue] = Option.empty
    if (bddDb.isDefined && mongoDbIsEnabled) {
      story = bddDb.get.findStory(storyPath)
    } else {
      story = findStoryFromFile(file, storyPath)
    }
    if (story.isEmpty) {
      Option(storyToJson("", storyPath, ""))
    } else {
      story
    }
  }

  def findStories(dir: File, storiesRootDir: String, storiesPath: String): Option[JsValue] = {
    val fullPath = s"$storiesRootDir/$storiesPath"
    val formattedPath = if (fullPath.endsWith("/")) fullPath else s"$fullPath/"
    var dirs = Seq[String]()
    var files = Seq[String]()
    if (bddDb.isDefined && mongoDbIsEnabled) {
      dirs = bddDb.get.findStoryDirPaths(formattedPath)
        .map(_.replace(formattedPath, ""))
    } else if (bddFile.isDefined) {
      dirs = bddFile.get.listDirs(dir)
      files = bddFile.get.listFiles(dir).filter(_.endsWith(".story")).map(_.replace(".story", ""))
    }
    Option(Json.toJson(Map(
      "stories" -> Json.toJson(files.map(file => Json.toJson(Map("name" -> Json.toJson(file))))),
      "dirs" -> Json.toJson(dirs
        .map(path => if (path.endsWith("/")) path.dropRight(1) else path)
        .map(dir => Json.toJson(Map("name" -> Json.toJson(dir))))
      ))
    ))
  }

  def storiesFromFileToMongoDb(dir: String): Boolean = {
    if (bddFile.isDefined && bddDb.isDefined) {
      val storyPaths = bddFile.get
        .listFiles(new File(dir), recursive = true, extension = Option(".story"))
        .map(dir + "/" + _)
      val storyJsons = storyPaths.map(storyPath => findStoryFromFile(new File(storyPath), storyPath))
      storyJsons.filter(_.isDefined).map(json => bddDb.get.upsertStory(json.get))
      true
    } else {
      false
    }
  }

  private[models] def findStoryFromFile(file: File, storyPath: String): Option[JsValue] = {
    var story: Option[JsValue] = Option.empty
    if (bddFile.isDefined) {
      val storyString = bddFile.get.fileToString(file)
      if (storyString.isDefined) {
        val storyName = file.getName.split('.').init.mkString(".")
        story = Option(storyToJson(storyName, storyPath, storyString.get))
      }
    }
    story
  }

}

// TODO Remove dir and path
object Story {

  def apply(dir: String = "",
            path: String = "",
            bddFile: Option[BddFile] = Option.empty,
            bddDb: Option[BddDb] = Option.empty): Story = {
    new Story(dir, path, bddFile, bddDb)
  }

}
