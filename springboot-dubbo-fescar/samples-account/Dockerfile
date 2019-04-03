# 选择JDK
FROM openjdk:8u212-jre-slim-stretch
VOLUME /tmp
ADD target/samples-account-1.0.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
# 命令行
# 构建 docker build -t samples-account:1.0.0 .
# 测试运行 docker run --net=host samples-account:1.0.0 /bin/bash
# 进入bash docker exec -it [container_id] bash
# 后台运行(开发) docker run -d --net=host samples-account:1.0.0
