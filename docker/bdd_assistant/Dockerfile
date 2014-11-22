# Version: 0.1
FROM ubuntu:14.04
MAINTAINER Viktor Farcic "viktor@farcic.com"

# General
RUN apt-get update
RUN apt-get -y install wget

# Java
RUN apt-get -y install --no-install-recommends openjdk-7-jdk

# Scala
RUN wget www.scala-lang.org/files/archive/scala-2.11.2.deb
RUN dpkg -i scala-2.11.2.deb
RUN rm scala-2.11.2.deb

# SBT
RUN wget https://dl.bintray.com/sbt/debian/sbt-0.13.6.deb
RUN dpkg -i sbt-0.13.6.deb
RUN rm sbt-0.13.6.deb

# GIT
WORKDIR /opt/
RUN apt-get -y install git

# Compile
ENV REFRESHED_AT 2014-09-28
RUN git clone https://github.com/TechnologyConversations/TechnologyConversationsBdd.git
WORKDIR /opt/TechnologyConversationsBdd/
RUN sbt stage

# Clean
RUN rm -rf app docker less project test
RUN rm -f .git .gitignore .travis.yml bower.json Gruntfile.js gulpfile.js package.json Procfile scalastyle-config.xml

# Run
EXPOSE 9000
CMD ["target/universal/stage/bin/tcbdd"]
