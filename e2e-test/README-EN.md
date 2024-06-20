## Seata E2E Test Quick Start
### Prepare test dependency framework.
E2E testing depends on the third-party testing framework [apache/skywalking-infra-e2e](https://github.com/apache/skywalking-infra-e2e). If Go is already installed in your environment, you can use the following command for installation:
```yaml
cd seata-samples
cd e2e-test/scripts
sh prepare_skywalkingE2E.sh
```
### Build test files
Use the following command to build the image and test files. After building, the test files will be located in `seata-samples/tmp`.
```
cd seata-samples
cd e2e-test/scripts
sh prepare_skywalkingE2E.sh
```
### Run test cases
Use the following command to run the scenes. This command will sequentially execute scene tests based on all test files under `seata-samples/tmp`.
```
cd seata-samples
cd e2e-test/scripts
sh test-run.sh
```
### Add test cases
The test case config file is named `seata-e2e.yaml` and should be placed under the basedir of each project that needs testing.
Under the basedir, create an `e2e-files` directory. Place the files that need to be mounted in the container during testing,
as well as the expected verification results and other files used during the test, into this directory.
This folder will be placed under the directory of the test case along with the construction of the test case.
For specific usage examples, refer to `at-sample/springboot-dubbo-seata`.
### Test framework principles
The Seata E2E framework identifies projects in the `samples` directory that require testing, builds Docker images, Docker-compose files,
and files required for testing. It uses the E2E testing framework [apache/skywalking-infra-e2e](https://github.com/apache/skywalking-infra-e2e)
to test according to the generated test files. GitHub Actions are deployed to automatically test all test cases.

- Runs containers based on Docker-compose file.
- The `e2e-test-builder` module builds images under `seata-samples` and generates test files used by the testing framework.
- The `e2e-test-runner` module identifies test cases built by `e2e-test-builder` and sequentially validates them using the E2E testing framework.

#### Test steps
Below are the automated steps for test. You only need to understand them; manual execution is not required.
For each test case, the following steps will be performed:
- Automated image construction based on the `seata-e2e.yaml` in test project. Relevant files will be generated under `tmp/images` during construction.
- Copy the `e2e-files` directory from the test project and generate docker-compose files and `e2e.yaml` files for testing based on `seata-e2e.yaml`.
- Execute `e2e.yaml` file using the E2E testing framework to initiate testing for a test.
- Await the success of the test results.

