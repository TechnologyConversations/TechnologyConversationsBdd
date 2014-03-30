package models.file

import java.io.{PrintWriter, File}
import scala.io.Source
import scalax.file.Path

trait BddFile {

  // TODO Change for method params
  val dir: String
  // TODO Change for method params
  val path: String
  // TODO Change for method params
  lazy val fullPath: String = s"$dir/$path"

  def content: String = {
    val file = new File(fullPath)
    if (fullPath.isEmpty || !file.exists || file.isDirectory) { "" }
    else { Source.fromFile(fullPath).mkString }
  }

  def renameFrom(originalPath: String): Boolean = {
    new File(s"$dir/$originalPath").renameTo(new File(fullPath))
  }

  def save(filePath: String, content: String, overwrite: Boolean): Boolean = {
    val file = new File(filePath)
    if (file.exists && !overwrite) {
      false
    } else {
      file.getParentFile.mkdirs()
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

  def list(): List[String] = {
    list(fullPath)
  }

  private[file] def list(path: String): List[String] = {
    val file = new File(path)
    if (file.exists()) {
      val files = file.listFiles()
      val filesInCurrentDir = files.filter(_.isFile).map(_.getPath).toList
      val filesInSubDirs = files.filter(_.isDirectory).flatMap(file => list(file.getPath))
      filesInCurrentDir ++ filesInSubDirs
    } else {
      List()
    }
  }

}
