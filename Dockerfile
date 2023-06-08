FROM openjdk:17.0.1-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} budsdom.jar
ENTRYPOINT ["java","-jar","/budsdom.jar"]
