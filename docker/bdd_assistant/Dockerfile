# Version: 0.1
FROM ubuntu:14.04
MAINTAINER Viktor Farcic "viktor@farcic.com"

# General
RUN apt-get update

# Browsers
RUN apt-get -y install wget && \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo 'deb http://dl.google.com/linux/chrome/deb/ stable main' >> /etc/apt/sources.list && \
    apt-get update && \
    apt-get -y install phantomjs && \
    apt-get -y install firefox && \
    apt-get -y install google-chrome-stable && \
    dpkg -r wget && \
    apt-get -y autoremove && \
    apt-get clean all

# BDD Assistant
RUN apt-get -y install wget && \
    apt-get -y install --no-install-recommends openjdk-7-jdk && \
    wget www.scala-lang.org/files/archive/scala-2.11.2.deb && \
    dpkg -i scala-2.11.2.deb && \
    rm scala-2.11.2.deb && \
    wget https://dl.bintray.com/sbt/debian/sbt-0.13.6.deb && \
    dpkg -i sbt-0.13.6.deb && \
    rm sbt-0.13.6.deb && \
    cd /opt/ && \
    apt-get -y install git && \
    git clone https://github.com/TechnologyConversations/TechnologyConversationsBdd.git && \
    cd /opt/TechnologyConversationsBdd/ && \
    sbt stage && \
    dpkg -r wget && \
    dpkg -r scala && \
    dpkg -r git && \
    dpkg -r sbt && \
    apt-get -y autoremove && \
    apt-get clean all

# Run
RUN mkdir -p /opt/TechnologyConversationsBdd/target/universal/stage/build/reports/tests/
WORKDIR /opt/TechnologyConversationsBdd/
EXPOSE 9000
CMD ["target/universal/stage/bin/tcbdd"]
