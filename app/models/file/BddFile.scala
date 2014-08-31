package models.file

import java.io.File

import org.apache.commons.io.FileUtils

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

  private[file] def writeStringToFile(file: File, content: String): Unit = {
    FileUtils.writeStringToFile(file, content, "UTF-8")
  }

}

object BddFile {

  def apply() = new BddFile()

}
