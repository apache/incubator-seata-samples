<#--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->

FROM ${baseImage!"eclipse-temurin:8-jdk-alpine"}
RUN apk --no-cache add curl bash && \
    mkdir -p /seata-e2e && \
    chmod 777 /seata-e2e
WORKDIR /seata-e2e
COPY ${sourceJar} app.jar

# Create startup script with JDK version logging
RUN printf '#!/bin/bash\n\
echo "=========================================="\n\
echo "Seata E2E Test Container Starting..."\n\
echo "=========================================="\n\
echo "Base Image: ${baseImage!"openjdk:8-jdk-alpine"}"\n\
echo "Java Version:"\n\
java -version 2>&1\n\
echo "=========================================="\n\
echo "Container Info:"\n\
echo "Hostname: $(hostname)"\n\
echo "Start Time: $(date)"\n\
echo "=========================================="\n\
echo "Starting Application..."\n\
JAVA_VERSION_STRING=$(java -version 2>&1 | head -n 1 | cut -d \\" -f 2)\n\
JAVA_VERSION=$(echo "$JAVA_VERSION_STRING" | cut -d . -f 1)\n\
if [ "$JAVA_VERSION" = "1" ]; then\n\
  JAVA_VERSION=$(echo "$JAVA_VERSION_STRING" | cut -d . -f 2)\n\
fi\n\
JAVA_OPTS="$JAVA_OPTS"\n\
if [ "$JAVA_VERSION" -ge 9 ] 2>/dev/null; then\n\
  JAVA_OPTS="$JAVA_OPTS --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED"\n\
fi\n\
exec java $JAVA_OPTS -jar app.jar\n' > /startup.sh && \
    chmod +x /startup.sh

ENTRYPOINT ["/startup.sh"]
