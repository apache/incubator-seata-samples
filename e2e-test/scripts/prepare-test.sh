#!/bin/bash

#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

echo "=========================================="
echo "Start Preparing Seata E2E Test Scenes"
echo "=========================================="

# Print environment information
echo "[INFO] Build Environment:"
echo "  - Build Time: $(date)"
echo "  - Java Version: $(java -version 2>&1 | head -n 1)"
if [ -n "$E2E_JDK_BASE_IMAGE" ]; then
  echo "  - Docker Base Image: $E2E_JDK_BASE_IMAGE"
else
  echo "  - Docker Base Image: openjdk:8-jdk-alpine (default)"
fi
echo "=========================================="

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TEST_DIR="$(dirname "$DIR")"
PROJECT_DIR="$(dirname "$(dirname "$DIR")")"

echo "[INFO] Building e2e-test-builder module..."
cd $TEST_DIR/e2e-test-builder
mvn clean install -DskipTests
result=$?
if [ $result -ne 0 ]; then
  echo "[ERROR] Build seata e2e-test-builder failure"
  exit $result
fi

echo "[INFO] Copying e2e-test-builder jar..."
cd $PROJECT_DIR
cp $TEST_DIR/e2e-test-builder/target/e2e-test-builder-*-jar-with-dependencies.jar $PROJECT_DIR/e2e-test-builder.jar

echo "[INFO] Generating test scenes and Docker images..."
java -jar ./e2e-test-builder.jar ./

echo "=========================================="
echo "Finish Preparing Seata E2E Test Scenes"
echo "=========================================="