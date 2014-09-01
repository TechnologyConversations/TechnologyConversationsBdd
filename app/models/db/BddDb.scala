package models.db

import com.mongodb.casbah.Imports._

class BddDb(val mongoIp: String, val mongoPort: Integer, val mongoDb: String) {

  val storiesCollection = "stories"
//  val mongoIp = Play.current.configuration.getString("db.mongodb.ip").getOrElse("localhost")
//  val mongoPort = Play.current.configuration.getInt("db.mongodb.port").getOrElse(27017)
//  val mongoDb = Play.current.configuration.getString("db.mongodb.db").getOrElse("tcbdd")

  def upsertStory(query: MongoDBObject, update: MongoDBObject): Boolean = {
    val coll = collection(storiesCollection)
    val result = coll.update(query, update, upsert = true)
    if (result == null) false else result.getN > 0
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