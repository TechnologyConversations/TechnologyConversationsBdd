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

**[Gulp](http://gulpjs.com/)**

```bash
npm install -g grunt-cli
npm install -g gulp
npm install -D gulp-jasmine
npm install
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
npm install
```

npm install adds two directories:

* public/node_modules
* public/bower_components


Development
-----------

Front-end files need to pass the process of concatenation, uglification, annotation, testing...

To prepare front-end files execute:

```bash
gulp
```

To continuously run gulp js task, execute:

```bash
gulp watch
```


Running the application
-----------------------

To run the application in development mode (re-compiles with every change to the code):

```bash
sbt run
```

To compile and run the application in production mode

```bash
sbt stage
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

### Front-end

All front-end JS unit tests are run as part of **gulp**.
Alternative ways to run tests are described below.

Front-end unit testing

```bash
npm test
```

Front-end unit testing without installation and dependencies

```bash
grunt jasmine
```


### Back-end

Back-end unit testing

```bash
sbt ~test-quick
```


Functional Tests
----------------

Directory where PhantomJS, ChromeDriver and IEDriverServer are located must be in the system path.
