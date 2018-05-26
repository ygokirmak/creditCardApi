FROM openjdk:8-jre-alpine

ENTRYPOINT /usr/bin/java ${JAVA_OPTIONS} -jar /usr/share/myservice/myservice.jar

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/myservice/myservice.jar