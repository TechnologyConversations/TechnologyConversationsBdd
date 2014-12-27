To run the main container:

```bash
docker run -d -p 9000:9000 --name bdd vfarcic/bdd
```

To run the main container with stories, composites and screenshots directories mapped outside the container:

```bash
STORIES_PATH=/data/bdd/data/stories
COMPOSITES_PATH=/data/bdd/composites
SCREENSHOTS_PATH=/data/bdd/screenshots
mkdir -p $STORIES_PATH $COMPOSITES_PATH $SCREENSHOTS_PATH
docker run -d -p 9000:9000 --name bdd \
  -v $STORIES_PATH:/opt/bdd/data/stories \
  -v $COMPOSITES_PATH:/opt/bdd/composites \
  -v $SCREENSHOTS_PATH:/opt/bdd/build/reports/tests \
  vfarcic/bdd
```

To run the application with MongoDB (still under development):

```bash
MONGODB_DATA_PATH=/var/lib/bdd/data/mongodb
docker run -d -p 27017:27017 -v $MONGODB_DATA_PATH:/data/db --name bdd_mongodb vfarcic/bdd_assistant_mongodb
```

Open the [http://localhost:9000](http://localhost:9000) in you favorite browser.

At the moment Docker container supports only PhantomJS browser

Please visit [BDD Assistant](http://bddassistant.com) and the GitHub [repository](https://github.com/TechnologyConversations/TechnologyConversationsBdd) for more info.