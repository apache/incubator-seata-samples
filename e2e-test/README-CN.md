## Seata E2E Test Quick Start
### 准备测试依赖框架
E2E测试依赖三方测试框架  [ apache/skywalking-infra-e2e](https://github.com/apache/skywalking-infra-e2e)
如运行环境已安装go，可使用以下命令安装
```yaml
cd seata-samples
cd e2e-test/scripts
sh prepare_skywalkingE2E.sh
```
### 构建测试文件
使用以下命令构建镜像，测试文件，构建完后的测试文件在`seata-samples/tmp`下
```
cd seata-samples
cd e2e-test/scripts
sh prepare_test.sh
```
### 运行测试案例
使用以下命令运行案例，该命令会在`seata-samples/tmp`下根据所有测试文件依次进行案例测试
```
cd seata-samples
cd e2e-test/scripts
sh test-run.sh
```
### 添加测试用例
测试用例配置文件名为：`seata-e2e.yaml`，放在每个需要测试的工程basedir下。
在basedir下创建`e2e-files`目录，将需在容器中挂载的文件，E2E测试的期待验证结果等测试期间使用的文件放入其中。支持构建镜像时进行原项目文件的替换。可配置具体使用案例可见`at-sample/springboot-dubbo-seata`和`at-sample/at-api`
### 测试框架原理
seata e2e框架会将samples工程中需要测试的项目进行识别，构建docker镜像，docker-compose文件及测试所依赖的文件。并使用E2E测试框架[ apache/skywalking-infra-e2e](https://github.com/apache/skywalking-infra-e2e)根据所生成测试文件进行测试。目前已部署github action对所有测试用例进行自动测试。

- 基于docker-compose 以容器方式运行
- e2e-test-builder 模块构建seata-samples下镜像，生成测试框架所使用的测试文件
- e2e-test-runner 模块找到e2e-test-builder构建的测试案例，依次使用E2E测试框架进行案例验证
#### 测试步骤
下面是脚本自动完成的步骤，只需要理解，不需要手工执行。
对每个测试案例来说，都会按照下面的步骤进行处理。

- 根据测试项目和`seata-e2e.yaml`自动进行镜像构建，构建过程中会在`tmp/images`下生成相关文件
- 拷贝测试项目下`e2e-files`目录，根据`seata-e2e.yaml`生成docker-compose文件和e2e.yaml文件用于测试
- 使用E2E测试框架执行e2e.yaml文件，开启一个案例下的测试
- 等待测试结果是否成功
