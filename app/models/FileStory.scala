package models

import java.io.File
import scala.io.Source

trait FileStory {

  val path: String

  def name: String = new File(path).getName.split('.').init.mkString(".")

  def content: String = {
    if (path.isEmpty) ""
    else Source.fromFile(path).mkString
  }

}
