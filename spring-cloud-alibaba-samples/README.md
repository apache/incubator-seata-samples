# spring-cloud-alibaba-samples

## 准备环境

* 启动 Nacos server <br>
  [Nacos Server 下载地址](https://github.com/alibaba/nacos/releases)

需要2.0以上

> 下载最新版本Nacos Server, 本地启动Nacos

* 启动Seata Server <br>
  [Seata Server 下载地址](https://github.com/seata/seata/releases)

> 下载最新版本Seata Server, 本地启动Seata

## 使用组件介绍

* Nacos 注册中心
* Nacos 配置中心
* Dubbo RPC 服务调用
* Open Feign REST 服务调用
* Sentinel 限流熔断
* Seata 分布式事务解决方案

## 项目目录介绍

* sca-common <br>
  `项目公用模块（实体类，Dubbo Api等）`
* sca-customer 消费者
* sca-provider 服务提供者

