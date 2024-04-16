#!/bin/bash

echo "start prepare Seata e2e test scene"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TEST_DIR="$(dirname "$DIR")"
PROJECT_DIR="$(dirname "$(dirname "$DIR")")"
cd $TEST_DIR/e2e-test-builder
mvn clean install -DskipTests
result=$?
if [ $result -ne 0 ]; then
  echo "Build seata e2e-test-builder failure"
  exit $result
fi
cd $PROJECT_DIR
cp $TEST_DIR/e2e-test-builder/target/e2e-test-builder-*-jar-with-dependencies.jar $PROJECT_DIR/e2e-test-builder.jar
java -jar ./e2e-test-builder.jar ./
echo "finish prepare Seata e2e test scene"