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

  def post(content: String): Boolean = {
    val file = new File(path)
    if (file.exists) return false
    val writer = new PrintWriter(file)
    writer.write(content)
    writer.close()
    true
  }


}
