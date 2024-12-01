FROM openjdk:17-jdk-slim

EXPOSE 8080/tcp

COPY target/apache-wicket-initializr.jar /apache-wicket-initializr.jar
ENTRYPOINT ["java", "-jar", "/apache-wicket-initializr.jar"]
