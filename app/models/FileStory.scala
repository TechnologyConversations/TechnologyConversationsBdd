package models

import java.io.{PrintWriter, File}
import scala.io.Source

trait FileStory {

  val path: String

  def name: String = new File(path).getName.split('.').init.mkString(".")

  def content: String = {
    if (path.isEmpty) ""
    else Source.fromFile(path).mkString
  }

  def put(content: String) = {
    val writer = new PrintWriter(new File(path))
    writer.write(content)
    writer.close()
  }


}
