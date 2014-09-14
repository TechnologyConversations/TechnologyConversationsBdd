package models.file

import java.io.File

import org.apache.commons.io.FileUtils

import scala.io.Source

class BddFile {

  def saveFile(file: File, content: String, overwrite: Boolean): Boolean = {
    if (file.exists && !overwrite) {
      false
    } else {
      val parentDir = file.getParentFile
      if (parentDir != null) {
        parentDir.mkdirs()
      }
      writeStringToFile(file, content)
      true
    }
  }

  def deleteFile(file: File): Boolean = {
    file.delete()
  }

  def fileToString(file: File): Option[String] = {
    if (!file.exists || file.isDirectory) {
      Option.empty
    } else {
      val fileSource = sourceFromFile(file)
      val stringSource = fileSource.mkString
      fileSource.close()
      Option(stringSource)
    }
  }

  private[file] def sourceFromFile(file: File) = {
    Source.fromFile(file, "UTF-8")
  }

  private[file] def writeStringToFile(file: File, content: String) {
    FileUtils.writeStringToFile(file, content, "UTF-8")
  }

}

object BddFile {

  def apply() = new BddFile()

}
