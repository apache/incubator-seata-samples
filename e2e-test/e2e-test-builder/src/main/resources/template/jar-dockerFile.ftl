FROM java:8
COPY ${sourceJar} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]