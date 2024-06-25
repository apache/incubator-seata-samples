#!/bin/bash

echo "start run Seata e2e test scene"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TEST_DIR="$(dirname "$DIR")"
PROJECT_DIR="$(dirname "$(dirname "$DIR")")"
cd $TEST_DIR
mvn clean install -DskipTests
result=$?
if [ $result -ne 0 ]; then
  echo "Build seata e2e-test failure"
  exit $result
fi
cd $TEST_DIR/e2e-test-runner
mvn clean install -DskipTests
result=$?
if [ $result -ne 0 ]; then
  echo "Build seata e2e-test-runner failure"
  exit $result
fi
cd $PROJECT_DIR
cp $TEST_DIR/e2e-test-runner/target/e2e-test-runner-*-jar-with-dependencies.jar $PROJECT_DIR/e2e-test-runner.jar
echo "start run seata test by skywalking e2e framework"
pwd
java -jar ./e2e-test-runner.jar ./tmp/scene-test
result=$?
if [ $result -ne 0 ]; then
  echo "run seata e2e-test-runner failure"
  exit $result
fi
echo "finish run Seata e2e test scene"