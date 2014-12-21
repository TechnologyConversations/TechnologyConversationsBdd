# Version: 0.1
FROM ubuntu:14.04
MAINTAINER Viktor Farcic "viktor@farcic.com"

# General
RUN apt-get update

# Browsers
RUN apt-get -y install phantomjs && \
    apt-get -y install firefox && \
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    sudo dpkg -i google-chrome*.deb && \
    rm google-chrome*.deb && \
    deb http://deb.opera.com/opera/ stable non-free && \
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
