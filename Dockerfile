FROM ubuntu:cosmic AS build

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update -q \
 && apt-get upgrade -q -y \
 && apt-get install -q -y \
        binutils \
        openjdk-11-jdk-headless

RUN mkdir /dougbank

WORKDIR /dougbank

ADD ./target/money-transfer-api-1.0-SNAPSHOT.jar money-transfer-api.jar

EXPOSE 8081

CMD java -jar money-transfer-api.jar

