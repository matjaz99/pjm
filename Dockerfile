# Build stage
FROM maven:3.8.1-openjdk-11 AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Package stage
#FROM tomcat:8.5-jre8-alpine
FROM tomcat:8.5.93-jdk17
#FROM tomcat:9.0.64-jre11-openjdk

RUN apk --no-cache add curl
RUN apk add tzdata

COPY --from=build  /home/app/target/pjm.war /usr/local/tomcat/webapps/pjm.war

RUN mkdir -p /opt/pjm/log
RUN mkdir -p /opt/pjm/config
RUN mkdir -p /opt/pjm/projects
COPY LICENSE /opt/pjm
COPY README.md /opt/pjm
#COPY CHANGELOG.md /opt/pjm

EXPOSE 8080
