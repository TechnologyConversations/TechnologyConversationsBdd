package models.db

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import com.mongodb.util.JSON

class BddDb(val mongoIp: String, val mongoPort: Integer, val mongoDb: String) {

  val storiesCollection = "stories"
  val storyDirectoriesCollection = "storyDirectories"

  def upsertStory(story: JsValue): Boolean = {
    val coll = collection(storiesCollection)
    val storyPath = (story \ "path").as[String]
    val result = coll.update(
      DBObject("_id" -> storyPath),
      jsValueToMongoDbObject(story),
      upsert = true)
    if (result == null) false else result.getN > 0
  }

  def removeStory(storyPath: String): Boolean = {
    val coll = collection(storiesCollection)
    val result = coll.remove(DBObject("_id" -> storyPath))
    if (result == null) false else result.getN > 0
  }

  private[db] def jsValueToMongoDbObject(json: JsValue): MongoDBObject = {
    JSON.parse(json.toString()).asInstanceOf[BasicDBObject]
  }

  private[db] def collection(mongoCollection: String): MongoCollection = {
    val client = MongoClient(mongoIp, mongoPort)
    val db = client(mongoDb)
    db(mongoCollection)
  }

}

object BddDb {

  def apply(mongoIp: String, mongoPort: Integer, mongoDb: String) = new BddDb(mongoIp, mongoPort, mongoDb)

}