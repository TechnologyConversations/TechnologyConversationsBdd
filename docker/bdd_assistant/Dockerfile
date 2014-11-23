# Version: 0.1
FROM ubuntu:14.04
MAINTAINER Viktor Farcic "viktor@farcic.com"

# General
RUN apt-get update

RUN # wget
    apt-get -y install wget
    # Java && \
    apt-get -y install --no-install-recommends openjdk-7-jdk && \
    wget www.scala-lang.org/files/archive/scala-2.11.2.deb && \
    dpkg -i scala-2.11.2.deb && \
    rm scala-2.11.2.deb && \
    # SBT && \
    wget https://dl.bintray.com/sbt/debian/sbt-0.13.6.deb && \
    dpkg -i sbt-0.13.6.deb && \
    rm sbt-0.13.6.deb && \
    # GIT && \
    cd /opt/ && \
    apt-get -y install git && \
    # Compile && \
    git clone https://github.com/TechnologyConversations/TechnologyConversationsBdd.git && \
    cd /opt/TechnologyConversationsBdd/ && \
    sbt stage && \
    # Cleanup && \
    dpkg -r wget && \
    dpkg -r scala && \
    dpkg -r git && \
    dpkg -r sbt && \

# Run
WORKDIR /opt/TechnologyConversationsBdd/
EXPOSE 9000
CMD ["target/universal/stage/bin/tcbdd"]
