FROM openjdk:8-jdk-alpine
COPY ${sourceJar} /app.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]