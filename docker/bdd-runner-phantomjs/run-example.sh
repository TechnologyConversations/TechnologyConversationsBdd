#!/bin/bash

sudo docker run -it --rm vfarcic/bdd-runner-phantomjs \
  --story_path data/stories/tcbdd/stories/storyEditorForm.story \
  --composites_path /opt/bdd/composites/TcBddComposites.groovy \
  -P url=http://demo.bddassistant.com -P widthHeight=1024,768
