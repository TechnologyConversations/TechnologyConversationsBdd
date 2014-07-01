TC Build Assistant
=====================================

[![Build Status](https://travis-ci.org/TechnologyConversations/TechnologyConversationsBdd.png?branch=master)](https://travis-ci.org/TechnologyConversations/TechnologyConversationsBdd)


Development prerequisites
-------------------------

### Back-end

**Scala**
**Play**
**SBT**

### Front-end

**[NodeJS with NPM](http://nodejs.org/)**

**[Grunt](http://gruntjs.com/)**

```bash
npm install -g grunt-cli
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


Unit Tests
----------

Front-end unit  testing

```bash
cd public
npm test
```

Back-end unit testing

```bash
play ~test-quick
```


Functional Tests
----------------

Directory where chromedriver and IEDriverServer are located must be in the system path.
