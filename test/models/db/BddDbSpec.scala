package models.db

import org.specs2.matcher.JsonMatchers
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json
import org.mockito.Mockito.doReturn

class BddDbSpec extends Specification with Mockito with JsonMatchers {

  val storiesCollection = mock[MongoCollection]
  val mongoIp = "MONGO_IP"
  val mongoPort = 1234
  val mongoDb = "MONGO_DB"
  val storyPath = "PATH/TO/MY.STORY"
  val storyDirectoryPath = "PATH/TO/MY/DIRECTORY"
  val updateJson = Json.parse("""{"path": "path/to/my.story", "key": "VALUE"}""")
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

    "call update on the stories collection" in {
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns storiesCollection
      bddDb.upsertStory(updateJson)
      there was one(storiesCollection).update(query, update, upsert = false)
    }

  }

  "BddDb#deleteStory" should {

    "call remove on the stories collection" in {
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns storiesCollection
      bddDb.removeStory(storyPath)
      there was one(storiesCollection).remove(query)
    }

  }

  "BddDb#findStory" should {

    "return result of findOneToJsValue" in {
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns storiesCollection
      val expected = Option(Json.parse("""{"key": "value"}"""))
      doReturn(expected).when(bddDb).findOneToJsValue(storiesCollection, query)
      bddDb.findStory(storyPath) must equalTo(expected)
    }

  }

  "BddDb#findStories" should {

    "return result of findToJsValueSeq" in {
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns storiesCollection
      val expected = Seq(Json.parse("""{"key": "value"}"""))
      doReturn(expected).when(bddDb).findToJsValueSeq(storiesCollection)
      bddDb.findStories() must equalTo(expected)
    }

  }

  "BddDb#findStoryNames" should {

    "return result of distinct" in {
      val directoryPath = "path/to/my/dir"
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns storiesCollection
      val expected = Seq("value1", "value2")
      doReturn(expected).when(bddDb).distinct(storiesCollection, "name", MongoDBObject("dirPath" -> directoryPath))
      bddDb.findStoryNames(directoryPath) must equalTo(expected)

    }

  }

  "BddDb#findStoryDirPaths" should {

    "return result of distinct" in {
      val directoryPath = "path/to/my/dir"
      val bddDb = spy(BddDb(mongoIp, mongoPort, mongoDb))
      bddDb.collection(bddDb.storiesCollection) returns storiesCollection
      val expected = Seq("value1", "value2")
      val regex = directoryPath + """[^\/]+/$"""
      doReturn(expected).when(bddDb).distinct(storiesCollection, "dirPath", "dirPath" $regex regex)
      bddDb.findStoryDirPaths(directoryPath) must equalTo(expected)

    }

  }

}
