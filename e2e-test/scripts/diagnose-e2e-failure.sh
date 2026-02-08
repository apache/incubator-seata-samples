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

# E2E Test Failure Diagnostic Script

SCENE_NAME=${1:-"saga-spring-seata-saga"}

echo "=========================================="
echo "E2E Test Failure Diagnostic Tool"
echo "=========================================="
echo "Diagnosing scene: $SCENE_NAME"
echo ""

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR="$(dirname "$(dirname "$DIR")")"
SCENE_DIR="$PROJECT_DIR/tmp/scene-test/$SCENE_NAME"

if [ ! -d "$SCENE_DIR" ]; then
    echo "[ERROR] Scene directory not found: $SCENE_DIR"
    echo "Please run prepare-test.sh first."
    exit 1
fi

cd "$SCENE_DIR"

echo "[INFO] Checking docker-compose.yaml..."
if [ -f "docker-compose.yaml" ]; then
    echo "✓ docker-compose.yaml exists"
    echo ""
    echo "--- docker-compose.yaml content (first 30 lines) ---"
    head -n 30 docker-compose.yaml
    echo ""
else
    echo "✗ docker-compose.yaml NOT found!"
    exit 1
fi

echo "[INFO] Checking generated Dockerfiles..."
IMAGE_DIRS=$(find "$PROJECT_DIR/tmp/images" -type d -name "${SCENE_NAME}*" 2>/dev/null)
if [ -n "$IMAGE_DIRS" ]; then
    for img_dir in $IMAGE_DIRS; do
        echo ""
        echo "--- Dockerfile in $img_dir ---"
        if [ -f "$img_dir/Dockerfile" ]; then
            cat "$img_dir/Dockerfile"
            echo ""
            
            # Try to build the image
            echo "[INFO] Testing Docker image build..."
            cd "$img_dir"
            docker build --no-cache -t "test-$(basename $img_dir)" . 2>&1 | tail -n 20
            BUILD_RESULT=${PIPESTATUS[0]}
            if [ $BUILD_RESULT -eq 0 ]; then
                echo "✓ Docker image built successfully"
            else
                echo "✗ Docker image build FAILED (exit code: $BUILD_RESULT)"
            fi
        fi
    done
fi

cd "$SCENE_DIR"

echo ""
echo "[INFO] Validating docker-compose configuration..."
docker-compose config > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✓ docker-compose.yaml syntax is valid"
else
    echo "✗ docker-compose.yaml has syntax errors:"
    docker-compose config 2>&1
    exit 1
fi

echo ""
echo "[INFO] Attempting to start services with detailed logs..."
echo "This will start the containers and show startup logs..."
echo ""

# Clean up any existing containers
docker-compose down -v 2>/dev/null

# Try to start with logs
timeout 60s docker-compose up --abort-on-container-exit 2>&1 | tee /tmp/docker-compose-diagnostic.log

echo ""
echo "=========================================="
echo "Diagnostic Summary"
echo "=========================================="

# Check for common issues in logs
if grep -q "port is already allocated" /tmp/docker-compose-diagnostic.log; then
    echo "⚠ Port conflict detected - some ports are already in use"
fi

if grep -q "No such file or directory" /tmp/docker-compose-diagnostic.log; then
    echo "⚠ Missing file detected - check volume mounts"
fi

if grep -q "executable file not found" /tmp/docker-compose-diagnostic.log; then
    echo "⚠ Script execution error - check entrypoint scripts"
fi

if grep -q "cannot execute binary file" /tmp/docker-compose-diagnostic.log; then
    echo "⚠ Binary compatibility issue - check base image architecture"
fi

echo ""
echo "[INFO] Full diagnostic log saved to: /tmp/docker-compose-diagnostic.log"
echo ""
echo "To manually inspect:"
echo "  cd $SCENE_DIR"
echo "  docker-compose up"
echo ""
echo "To check container logs:"
echo "  docker-compose logs <service_name>"
echo ""
echo "To clean up:"
echo "  cd $SCENE_DIR"
echo "  docker-compose down -v"
echo ""

