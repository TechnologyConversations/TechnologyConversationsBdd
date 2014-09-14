package models.db

import com.mongodb.casbah.Imports._
import play.api.libs.json._
import com.mongodb.util.JSON

class BddDb(val mongoIp: String, val mongoPort: Integer, val mongoDb: String) {

  lazy val client = MongoClient(mongoIp, mongoPort)
  lazy val db = client(mongoDb)
  val storiesCollection = "stories"

  def findStory(storyPath: String): Option[JsValue] = {
    val coll = collection(storiesCollection)
    findOneToJsValue(coll, DBObject("_id" -> storyPath))
  }

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

  private[db] def findOneToJsValue(collection: MongoCollection, query: MongoDBObject): Option[JsValue] = {
    val result = collection.findOne(query)
    if (result.isDefined) Option(Json.parse(result.get.toString)) else Option.empty
  }

  private[db] def collection(mongoCollection: String): MongoCollection = {
    db(mongoCollection)
  }

}

object BddDb {
  def apply(mongoIp: String, mongoPort: Integer, mongoDb: String) = new BddDb(mongoIp, mongoPort, mongoDb)
}