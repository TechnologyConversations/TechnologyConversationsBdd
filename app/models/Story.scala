package models

import models.db.BddDb
import models.jbehave.JBehaveStory
import models.file.BddFile
import java.io.File
import play.api.libs.json.{JsValue, Json}
import util.Imports._


class Story(val bddFile: Option[BddFile] = Option.empty,
            val bddDb: Option[BddDb] = Option.empty) extends JBehaveStory {

  // TODO Remove
  val mongoDbIsEnabled = featureIsEnabled("mongoDb")

  def saveStory(file: File, json: JsValue, overwrite: Boolean): Boolean = {
    if (bddDb.isDefined && mongoDbIsEnabled) {
      bddDb.get.upsertStory(json)
    } else if (bddFile.isDefined) {
      bddFile.get.saveFile(file, toText(json), overwrite)
    } else {
      false
    }
  }

  def removeStory(file: File, storyPath: String): Boolean = {
    if (bddDb.isDefined && mongoDbIsEnabled) {
      bddDb.get.removeStory(storyPath)
    } else if (bddFile.isDefined) {
      bddFile.get.deleteFile(file)
    } else {
      false
    }
  }

  def findStory(file: File, storyPath: String): Option[JsValue] = {
    val story: Option[JsValue] = {
      if (bddDb.isDefined && mongoDbIsEnabled) {
        bddDb.get.findStory(storyPath)
      } else {
        findStoryFromFile(file, storyPath)
      }
    }
    if (story.isEmpty) {
      Option(storyToJson("", storyPath, ""))
    } else {
      story
    }
  }

  def findStoryPaths(dir: File, storiesPath: String): Option[JsValue] = {
    val (dirs, files) = {
      if (bddDb.isDefined && mongoDbIsEnabled) {
        (bddDb.get.findStoryDirPaths(storiesPath), bddDb.get.findStoryNames(storiesPath))
      } else if (bddFile.isDefined) {
        (bddFile.get.listDirs(dir), bddFile.get.listFiles(dir).filter(_.endsWith(".story")).map(_.replace(".story", "")))
      } else {
        (Seq.empty[String], Seq.empty[String])
      }
    }
    Option(Json.toJson(Map(
      "stories" -> Json.toJson(files.map(file => Json.toJson(Map("name" -> Json.toJson(file))))),
      "dirs" -> Json.toJson(dirs
        .map(path => {
          val dir = if (path.endsWith("/")) path.dropRight(1) else path
          Json.toJson(Map("name" -> Json.toJson(dir)))
        })
      ))
    ))
  }

  def storiesFromFileToMongoDb(storiesPath: String): Boolean = {
    if (bddFile.isDefined && bddDb.isDefined) {
      bddFile.get
        .listFiles(new File(storiesPath), recursive = true, extension = Option(".story"))
        .map(storyPath => {
          val formattedPath = if (storyPath.startsWith("/")) storyPath.drop(1) else storyPath
          findStoryFromFile(new File(s"$storiesPath/$storyPath"), formattedPath)
        })
        .filter(_.isDefined)
        .foreach(json => bddDb.get.upsertStory(json.get))
      true
    } else {
      false
    }
  }

  def storiesFromMongoDbToFiles(exportDirPath: String): Boolean = {
    if (bddFile.isDefined && bddDb.isDefined) {
      bddDb.get.findStories()
        .foreach(json => {
          val path = (json \ "path").as[String]
          bddFile.get.saveFile(new File(s"$exportDirPath/$path"), toText(json), overwrite = true)
        })
      true
    } else {
      false
    }
  }

  private[models] def findStoryFromFile(file: File, storyPath: String): Option[JsValue] = {
    if (bddFile.isDefined) {
      val storyString = bddFile.get.fileToString(file)
      if (storyString.isDefined) {
        val storyName = file.getName.split('.').init.mkString(".")
        Option(storyToJson(storyName, storyPath, storyString.get))
      } else {
        Option.empty
      }
    } else {
      Option.empty
    }
  }

}

object Story {

  def apply(bddFile: Option[BddFile] = Option.empty, bddDb: Option[BddDb] = Option.empty): Story = {
    new Story(bddFile, bddDb)
  }

}
