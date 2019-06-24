FROM alpine:latest
MAINTAINER Ivan Malakhov <mail@ivanmalakhov.com>
USER root

RUN apk update
RUN apk fetch openjdk8
RUN apk add openjdk8

EXPOSE 4567

COPY target/lib /opt/lib
COPY target/moneytransfer-1.0-SNAPSHOT.jar /opt
WORKDIR /opt

ENTRYPOINT java -jar moneytransfer-1.0-SNAPSHOT.jar