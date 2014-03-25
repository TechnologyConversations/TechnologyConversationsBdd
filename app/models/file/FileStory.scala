package models.file

import java.io.File

trait FileStory extends BddFile {

  def name: String = new File(fullPath).getName.split('.').init.mkString(".")

}
