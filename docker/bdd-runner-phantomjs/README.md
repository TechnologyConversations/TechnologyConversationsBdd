Docker vfarcic/bdd-runner-phantomjs can be used to run BDD stories using PhantomJS browser.

```bash
sudo docker run -t --rm vfarcic/bdd-runner-phantomjs
```

Running it without any argument outputs help. An example run with few arguments would be:

```bash
sudo docker run -t --rm vfarcic/bdd-runner-phantomjs \
  --story_path data/stories/tcbdd/stories/storyEditorForm.story \
  --composites_path /opt/bdd/composites/TcBddComposites.groovy \
  -P url=http://demo.bddassistant.com -P widthHeight=1024,768
```

Stories, composites and screenshots directories can be mapped outside the container as explained in the [TechnologyConversations/TechnologyConversationsBdd repo](https://github.com/TechnologyConversations/TechnologyConversationsBdd).

Please visit [BDD Assistant](http://bddassistant.com) and the GitHub [repository](https://github.com/TechnologyConversations/TechnologyConversationsBdd) for more info.