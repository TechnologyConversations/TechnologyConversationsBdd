TC Build Assistant
=====================================

[![Build Status](https://travis-ci.org/TechnologyConversations/TechnologyConversationsBdd.png?branch=master)](https://travis-ci.org/TechnologyConversations/TechnologyConversationsBdd)


Development prerequisites
-------------------------

### Back-end

**[Scala](http://www.scala-lang.org/download/)**
**[Play/Activator](http://www.playframework.com/download)**
**[SBT](http://www.scala-sbt.org/download.html)**

### Front-end

**[NodeJS with NPM](http://nodejs.org/)**

**[Grunt](http://gruntjs.com/)**

```bash
npm install -g grunt-cli
npm install grunt-contrib-watch --save-dev
```


IDEA project
----------------------------

Use the gen-idea sbt task to create Idea project files.

```bash
sbt gen-idea
```

Dependencies
------------

Front-end dependencies can be installed by running following

```bash
cd public
npm install
```

npm install adds two directories:

* public/node_modules
* public/bower_components


Deployment to Heroku
--------------------

```bash
heroku create --stack cedar --buildpack https://github.com/ddollar/heroku-buildpack-multi.git
git push heroku master
```


Deployment to development environment
-------------------------------------

```bash
sbt run
```


Unit Tests
----------

Front-end unit testing

```bash
cd public
npm test
```

Front-end unit testing without installation and dependencies

```bash
cd public
grunt jasmine
```

Back-end unit testing

```bash
activator ~test-quick
```


Functional Tests
----------------

Directory where chromedriver and IEDriverServer are located must be in the system path.
