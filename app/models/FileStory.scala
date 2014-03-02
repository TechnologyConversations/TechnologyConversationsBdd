package models

import java.io.{PrintWriter, File}
import scala.io.Source
import scalax.file.Path

trait FileStory {

  val dir: String
  val path: String
  lazy val fullPath: String = s"$dir/$path"

  def name: String = new File(fullPath).getName.split('.').init.mkString(".")

  def content: String = {
    val file = new File(fullPath)
    if (fullPath.isEmpty || !file.exists ||file.isDirectory) { "" }
    else { Source.fromFile(fullPath).mkString }
  }

  def renameFrom(originalPath: String): Boolean = {
    new File(s"$dir/$originalPath").renameTo(new File(fullPath))
  }

  def save(content: String, overwrite: Boolean): Boolean = {
    val file = new File(fullPath)
    if (file.exists != overwrite) {
      false
    } else {
      val writer = new PrintWriter(file)
      writer.write(content)
      writer.close()
      true
    }
  }

  def delete: Boolean = {
    val file = new File(fullPath)
    if (file.exists) {
      if (file.isFile) {
        file.delete
      } else {
        val (deleted, remaining) = Path.fromString(file.getPath).deleteRecursively()
        remaining == 0
      }
    } else {
      true
    }
  }

  def createDirectory() {
    val file = new File(fullPath)
    if (!file.exists) {
      file.mkdir
    }
  }

}
