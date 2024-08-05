FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/xml-parser-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p src/main/resources/temp
ENTRYPOINT ["java", "-jar", "app.jar"]