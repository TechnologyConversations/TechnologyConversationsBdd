package models

import models.db.BddDb
import models.jbehave.JBehaveStory
import models.file.{BddFileTrait, BddFile}
import java.io.File
import play.api.libs.json.JsValue
import util.Imports._


// TODO Move methods from extended classes to objects set as constructor arguments
// TODO Remove dir and path
class Story(val dir: String = "",
            val path: String = "",
            val bddFile: Option[BddFile] = Option.empty,
            val bddDb: Option[BddDb] = Option.empty)
  extends JBehaveStory with BddFileTrait {

  override val name = ""

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
    } else if (bddFile.isDefined) {
      val storyString = bddFile.get.fileToString(file)
      if (storyString.isDefined) {
        val storyName = file.getName.split('.').init.mkString(".")
        story = Option(storyToJson(storyName, storyPath, storyString.get))
      }
    }
    if (story.isEmpty) {
      story = Option(storyToJson("", storyPath, ""))
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
