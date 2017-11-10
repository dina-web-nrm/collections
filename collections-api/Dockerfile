FROM openjdk:8u111-jdk-alpine

RUN apk update && apk add --no-cache tini openssl bash

COPY target/collections-api-swarm.jar /usr/src/myapp/
WORKDIR /usr/src/myapp
  
ENTRYPOINT ["/sbin/tini", "--"]
CMD java -jar collections-api-swarm.jar -Smysql
