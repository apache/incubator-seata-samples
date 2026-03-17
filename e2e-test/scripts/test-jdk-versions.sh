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

# This script tests different JDK versions for E2E testing

echo "=========================================="
echo "Seata E2E Multi-JDK Version Test Script"
echo "=========================================="

# Array of JDK versions to test
JDK_VERSIONS=("8" "11" "17")

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR="$(dirname "$(dirname "$DIR")")"

# Test each JDK version
for jdk_version in "${JDK_VERSIONS[@]}"
do
    echo ""
    echo "=========================================="
    echo "Testing with JDK Version: $jdk_version"
    echo "=========================================="
    
    export E2E_JDK_BASE_IMAGE="$jdk_version"
    
    echo "[INFO] Running prepare-test.sh with JDK $jdk_version..."
    cd "$DIR"
    sh prepare-test.sh
    
    if [ $? -ne 0 ]; then
        echo "[ERROR] prepare-test.sh failed for JDK $jdk_version"
        exit 1
    fi
    
    echo "[INFO] Checking generated Dockerfile..."
    # Find first generated Dockerfile and verify it contains the correct base image
    dockerfile=$(find "$PROJECT_DIR/tmp/images" -name "Dockerfile" -type f | head -n 1)
    if [ -n "$dockerfile" ]; then
        echo "[INFO] Sample Dockerfile content:"
        head -n 5 "$dockerfile"
        
        # Verify base image is correct
        if grep -q "FROM.*$jdk_version" "$dockerfile" || grep -q "FROM openjdk:$jdk_version" "$dockerfile"; then
            echo "[SUCCESS] ✓ Dockerfile contains correct JDK version"
        else
            echo "[WARNING] ⚠ Dockerfile may not contain expected JDK version"
        fi
    else
        echo "[WARNING] No Dockerfile found in tmp/images"
    fi
    
    echo "[INFO] Cleaning up tmp directory..."
    rm -rf "$PROJECT_DIR/tmp"
    
    echo "[SUCCESS] JDK $jdk_version test preparation completed"
done

echo ""
echo "=========================================="
echo "All JDK version tests completed!"
echo "=========================================="
echo ""
echo "Note: This script only tests the preparation phase."
echo "To run full E2E tests, use: sh test-run.sh"
echo ""

