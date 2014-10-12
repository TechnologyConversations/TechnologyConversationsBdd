package models.db

import com.mongodb.casbah.Imports._
import play.api.libs.json._
import com.mongodb.util.JSON

class BddDb(val mongoIp: String, val mongoPort: Integer, val mongoDb: String) {

  lazy val client = MongoClient(mongoIp, mongoPort)
  lazy val db = client(mongoDb)
  val storiesCollection = "stories"
  lazy val storiesMongoCollection = collection(storiesCollection)

  def findStory(storyPath: String): Option[JsValue] = {
    findOneToJsValue(storiesMongoCollection, DBObject("_id" -> storyPath))
  }

  def findStories(directoryPath: String): Seq[String] = {
    distinct(storiesMongoCollection, "name", MongoDBObject("dirPath" -> directoryPath))
  }

  def findStoryDirPaths(directoryPath: String): Seq[String] = {
    val regex = directoryPath + """[^\/]+/$"""
    distinct(storiesMongoCollection, "dirPath", "dirPath" $regex regex)
  }

  def upsertStory(story: JsValue): Boolean = {
    val storyPath = (story \ "path").as[String]
    val result = storiesMongoCollection.update(
      DBObject("path" -> storyPath),
      jsValueToMongoDbObject(story),
      upsert = true)
    if (result == null) false else result.getN > 0
  }

  def removeStory(storyPath: String): Boolean = {
    val result = storiesMongoCollection.remove(DBObject("_id" -> storyPath))
    if (result == null) false else result.getN > 0
  }

  private[db] def jsValueToMongoDbObject(json: JsValue): MongoDBObject = {
    JSON.parse(json.toString()).asInstanceOf[BasicDBObject]
  }

  private[db] def findOneToJsValue(collection: MongoCollection, query: MongoDBObject): Option[JsValue] = {
    val result = collection.findOne(query)
    if (result.isDefined) Option(Json.parse(result.get.toString)) else Option.empty
  }

  private[db] def distinct(collection: MongoCollection, key: String, query: MongoDBObject): Seq[String] = {
    val results = collection.distinct(key, query)
    results.toSeq.map(_.toString)
  }

  private[db] def collection(mongoCollection: String): MongoCollection = {
    db(mongoCollection)
  }

}

object BddDb {
  def apply(mongoIp: String, mongoPort: Integer, mongoDb: String) = new BddDb(mongoIp, mongoPort, mongoDb)
}