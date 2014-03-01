package models

import java.io.{PrintWriter, File}
import scala.io.Source
import scalax.file.Path

trait FileStory {

  val path: String

  def name: String = new File(path).getName.split('.').init.mkString(".")

  def content: String = {
    val file = new File(path)
    if (path.isEmpty || !file.exists ||file.isDirectory) { "" }
    else { Source.fromFile(path).mkString }
  }

  def rename(originalPath: String): Boolean = {
    new File(originalPath).renameTo(new File(path))
  }

  def save(content: String, overwrite: Boolean): Boolean = {
    val file = new File(path)
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
    val file = new File(path)
    if (file.exists) {
      if (file.isFile) {
        file.delete
      } else {
//        val filePath = Path.fromString(path.replace("/", File.separator))
        val filePath = Path.fromString(file.getPath)
        val (deleted, remaining) = filePath.deleteRecursively()
        remaining == 0
      }
    } else {
      true
    }
  }

  def createDirectory() {
    val filePath = Path(path)
    if (!filePath.exists) {
      filePath.createDirectory()
    }
  }

}
