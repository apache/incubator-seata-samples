FROM openjdk:8-jdk-alpine
COPY ${sourceJar} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]