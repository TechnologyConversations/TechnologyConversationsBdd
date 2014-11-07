package models.db

import com.mongodb.casbah.Imports._
import play.api.libs.json._
import com.mongodb.util.JSON

class BddDb(val mongoUri: String, val mongoDb: String) {

  lazy val client = MongoClient(MongoClientURI("mongodb://localhost:27017"))
  lazy val db = client(mongoDb)
  val storiesCollection = "stories"
  lazy val storiesMongoCollection = collection(storiesCollection)

  def findStory(storyPath: String): Option[JsValue] = {
    findOneToJsValue(storiesMongoCollection, DBObject("_id" -> storyPath))
  }

  def findStories(): Seq[JsValue] = {
    findToJsValueSeq(storiesMongoCollection)
  }

  def findStoryNames(directoryPath: String): Seq[String] = {
    val query = MongoDBObject("dirPath" -> formatDirectory(directoryPath))
    distinct(storiesMongoCollection, "name", query).sorted
  }

  def findStoryDirPaths(directoryPath: String): Seq[String] = {
    val paths = if (directoryPath.isEmpty) {
      distinct(storiesMongoCollection, "dirPath", MongoDBObject.empty)
    } else {
      val formattedPath = formatDirectory(directoryPath)
      val regex = formattedPath + """.+/$"""
      val prefixLength = formattedPath.length
      distinct(storiesMongoCollection, "dirPath", "dirPath" $regex regex)
        .map { result =>
          if (result.startsWith(formattedPath)) result.drop(prefixLength)
          else result
      }
    }
    paths.sorted
  }

  def upsertStory(story: JsValue): Boolean = {
    val storyPath = (story \ "path").as[String]
    val result = storiesMongoCollection.update(
      DBObject("_id" -> storyPath),
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

  private[db] def findToJsValueSeq(collection: MongoCollection): Seq[JsValue] = {
    collection.find().map(result => Json.parse(result.toString)).toSeq
  }

  private[db] def distinct(collection: MongoCollection, key: String, query: MongoDBObject = MongoDBObject.empty): Seq[String] = {
    val results = collection.distinct(key, query)
    results.toSeq.map(_.toString)
  }

  private[db] def collection(mongoCollection: String): MongoCollection = {
    db(mongoCollection)
  }

  private [db] def formatDirectory(path: String) = {
    if (path.endsWith("/")) path else s"$path/"
  }

}

object BddDb {
  def apply(mongoUri: String, mongoDb: String) = new BddDb(mongoUri, mongoDb)
}