TC Build Assistant
=====================================

[![Build Status](https://travis-ci.org/TechnologyConversations/TechnologyConversationsBdd.png?branch=master)](https://travis-ci.org/TechnologyConversations/TechnologyConversationsBdd)


Development prerequisites
-------------------------

### Back-end

* Scala
* Play
* SBT

### Front-end

* [NodeJS with NPM](http://nodejs.org/)


IDEA project
----------------------------

Use the gen-idea sbt task to create Idea project files.

´´´
$ sbt gen-idea
´´´

Dependencies
------------

Front-end dependencies can be installed by running following

```shell
cd public
npm install
```

npm install adds two directories:

* public/node_modules
* public/bower_components


Unit Tests
----------

Front-end unit  testing

```shell
cd public
npm test
```

Back-end unit testing

```shell
play ~test-quick
```


Functional Tests
----------------

Directory where chromedriver and IEDriverServer are located must be in the system path.
