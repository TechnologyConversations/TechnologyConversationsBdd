package models

import com.mongodb.casbah.MongoDB
import com.mongodb.casbah.commons.MongoDBObject
import models.db.BddDb
import models.jbehave.JBehaveStory
import models.file.FileStory

// TODO Move extended classes to constructor arguments
class Story(val dir: String = "", val path: String = "", val bddDb: Option[BddDb] = Option.empty)
  extends JBehaveStory with FileStory {

  // TODO Add BddFile#saveFile from StoryController
  def saveStory(): Boolean = {
    var dbSuccess = true
    if (bddDb.isDefined) {
      dbSuccess = bddDb.get.upsertStory()
    }
    dbSuccess
  }

}

object Story {

  def apply(dir: String, path: String): Story = new Story(dir, path)

}
