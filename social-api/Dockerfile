FROM openjdk:8-jdk-alpine
VOLUME /tmp

#Set app home folder
ENV APP_HOME /app
ENV JAVA_OPTS=""
#Create base app folder
RUN mkdir $APP_HOME && \
    #Create folder to save configuration files
   mkdir $APP_HOME/config && \
   #Create folder with application logs
   mkdir $APP_HOME/log

COPY target/social-api-1.0-SNAPSHOT-spring-boot.jar $APP_HOME/app.jar

VOLUME $APP_HOME/log
VOLUME $APP_HOME/config
WORKDIR $APP_HOME

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar $0 $@
