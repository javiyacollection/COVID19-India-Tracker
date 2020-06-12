FROM openjdk:8-jdk-alpine
ADD target/corona-tracker.jar corona-tracker.jar
ENTRYPOINT ["java","-jar", "/corona-tracker.jar"]