package models.db

import org.specs2.matcher.JsonMatchers
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json

class BddDbSpec extends Specification with Mockito with JsonMatchers {

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

    val mongoIp = "MONGO_IP"
    val mongoPort = 1234
    val mongoDb = "MONGO_DB"
    val coll = mock[MongoCollection]
    val storyPath = "PATH/TO/MY.STORY"
    val query = MongoDBObject("_id" -> storyPath)
    val update = DBObject("key" -> "VALUE")
    val updateJson = Json.parse("""{"key": "VALUE"}""")

    "call update on the stories collection" in {
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns coll
      bddDb.upsertStory(storyPath, updateJson)
      there was one(coll).update(query, update, upsert = false)
    }

  }

}
