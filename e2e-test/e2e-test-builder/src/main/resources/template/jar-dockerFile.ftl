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
RUN apk --no-cache add bash
COPY ${sourceJar} /app.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Log JDK version information on container start
RUN printf '#!/bin/bash\n\
echo "=========================================="\n\
echo "Seata E2E Test Container (Jar Mode)"\n\
echo "=========================================="\n\
echo "Base Image: ${baseImage!"openjdk:8-jdk-alpine"}"\n\
echo "Java Version:"\n\
java -version 2>&1\n\
echo "=========================================="\n\
echo "Container Info:"\n\
echo "Hostname: $(hostname)"\n\
echo "Start Time: $(date)"\n\
echo "=========================================="\n\
exec /entrypoint.sh\n' > /log-jdk-info.sh && \
    chmod +x /log-jdk-info.sh

ENTRYPOINT ["/log-jdk-info.sh"]