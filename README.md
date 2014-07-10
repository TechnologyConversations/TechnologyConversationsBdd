TC Build Assistant
=====================================

[![Build Status](https://travis-ci.org/TechnologyConversations/TechnologyConversationsBdd.png?branch=master)](https://travis-ci.org/TechnologyConversations/TechnologyConversationsBdd)

Running the project in development mode
---------------------------------------

Use play to run the BDDAssistant

´´´
$ activator run / sbt run
´´´

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

Running the application
-----------------------

To run the application in development mode (re-compiles with every change to the code):

```bash
activator run
```

To compile and run the application in production mode

```bash
activator stage
target/universal/stage/bin/tcbdd
```


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
