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
RUN git clone https://github.com/TechnologyConversations/TechnologyConversationsBdd.git

# Compile
ENV REFRESHED_AT 2014-09-28
WORKDIR /opt/TechnologyConversationsBdd/
RUN sbt stage
# RUN echo "#!/bin/bash\ncd /opt/TechnologyConversationsBdd/\ntarget/universal/stage/bin/tcbdd" >start_bdd_assistant.sh
# RUN chmod 777 start_bdd_assistant.sh
# ENV PATH $PATH:/opt/TechnologyConversationsBdd/
EXPOSE 9000
CMD ["target/universal/stage/bin/tcbdd"]

# sudo docker build -t="vfarcic/technologyconversationsbdd" .
# sudo docker run -t -i -p 9000:9000 --name bdd_assistant vfarcic/bdd_assistant
# sudo docker run -d -p 9000:9000 --name bdd_assistant vfarcic/bdd_assistant
# Remove NodeJS & NPM