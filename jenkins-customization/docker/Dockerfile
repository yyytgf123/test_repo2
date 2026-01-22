FROM jenkins/jenkins:lts-jdk17

USER root

## Docker Install ##
RUN apt-get update \
&& apt-get install -y docker.io \
&& rm -rf /var/lib/apt/lists/*

## Basic Util Tools ##
RUN apt-get update && apt-get install -y --no-install-recommends \
git curl ca-certificates && rm -rf /var/lib/apt/lists/*

## Plugin Install ##
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

USER jenkins
