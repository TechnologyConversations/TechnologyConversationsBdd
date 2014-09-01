package models.db

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import com.mongodb.casbah.Imports._

class BddDbSpec extends Specification with Mockito {

  "BddDbSpec#upsertStory" should {

    val mongoIp = "MONGO_IP"
    val mongoPort = 1234
    val mongoDb = "MONGO_DB"
    val coll = mock[MongoCollection]
    val query = MongoDBObject("_id" -> "MY_ID")
    val update = MongoDBObject("key" -> "VALUE")

    "call update on collection stories" in {
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns coll
      bddDb.upsertStory(query, update)
      there was one(coll).update(query, update, upsert = true)
    }

  }

}
