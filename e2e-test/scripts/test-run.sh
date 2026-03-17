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
echo "Start Running Seata E2E Test Scenes"
echo "=========================================="

# Print test environment information
echo "[INFO] Test Environment:"
echo "  - Test Time: $(date)"
echo "  - Java Version: $(java -version 2>&1 | head -n 1)"
if [ -n "$E2E_JDK_BASE_IMAGE" ]; then
  echo "  - Docker Runtime JDK: $E2E_JDK_BASE_IMAGE"
else
  echo "  - Docker Runtime JDK: openjdk:8-jdk-alpine (default)"
fi
echo "=========================================="

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TEST_DIR="$(dirname "$DIR")"
PROJECT_DIR="$(dirname "$(dirname "$DIR")")"

echo "[INFO] Building e2e-test module..."
cd $TEST_DIR
mvn clean install -DskipTests
result=$?
if [ $result -ne 0 ]; then
  echo "[ERROR] Build seata e2e-test failure"
  exit $result
fi

echo "[INFO] Building e2e-test-runner module..."
cd $TEST_DIR/e2e-test-runner
mvn clean install -DskipTests
result=$?
if [ $result -ne 0 ]; then
  echo "[ERROR] Build seata e2e-test-runner failure"
  exit $result
fi

echo "[INFO] Copying e2e-test-runner jar..."
cd $PROJECT_DIR
cp $TEST_DIR/e2e-test-runner/target/e2e-test-runner-*-jar-with-dependencies.jar $PROJECT_DIR/e2e-test-runner.jar

echo "=========================================="
echo "[INFO] Running tests by SkyWalking E2E framework"
echo "=========================================="
pwd
java -jar ./e2e-test-runner.jar ./tmp/scene-test
result=$?

echo "=========================================="
if [ $result -ne 0 ]; then
  echo "[ERROR] E2E test execution failed with exit code: $result"
  echo "=========================================="
  exit $result
else
  echo "[SUCCESS] All E2E tests passed!"
  echo "=========================================="
fi