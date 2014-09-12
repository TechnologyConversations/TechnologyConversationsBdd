package models.db

import org.specs2.matcher.JsonMatchers
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json

class BddDbSpec extends Specification with Mockito with JsonMatchers {

  val mongoIp = "MONGO_IP"
  val mongoPort = 1234
  val mongoDb = "MONGO_DB"
  val collection = mock[MongoCollection]
  val storyPath = "PATH/TO/MY.STORY"
  val query = MongoDBObject("_id" -> storyPath)

  "BddDb#jsValueToMongoDbObject" should {

    val json = Json.parse("""{"key": "value", "another_key": true}""")
    val bddDb = BddDb("", 0, "")

    "return MongoDbObject" in {
      bddDb.jsValueToMongoDbObject(json) must beAnInstanceOf[MongoDBObject]
    }

    "return MongoDbObjectWithJson" in {
      val jsonString = bddDb.jsValueToMongoDbObject(json).toString()
      jsonString must /("key" -> "value")
      jsonString must /("another_key" -> true)
    }

  }

  "BddDb#upsertStory" should {

    val update = DBObject("key" -> "VALUE")
    val updateJson = Json.parse("""{"path": "path/to/my.story", "key": "VALUE"}""")

    "call update on the stories collection" in {
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns collection
      bddDb.upsertStory(updateJson)
      there was one(collection).update(query, update, upsert = false)
    }

  }

  "BddDb#deleteStory" should {

    "call remove on the stories collection" in {
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns collection
      bddDb.removeStory(storyPath)
      there was one(collection).remove(query)
    }

  }

}
