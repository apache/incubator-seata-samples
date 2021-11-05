# SpringBoot-Dubbo使用 Seata 实现分布式事务 - MyBatisPlus动态数据源-Nacos为配置/注册中心

> 使用 Seata 作为分布式事务组件，使用 MySQL 数据库，使用 MyBatis 作为数据访问层实现多数据源下事务一致，使用 MyBatisPlus 作为 MyBatis 的辅助工具，Nacos为配置/注册中心

## 环境准备

### 创建数据库及表

根据sql文件夹内的sql文件名创建对应表，运行sql

### 安装Seata跟Nacos

按照该篇[文章](http://seata.io/zh-cn/blog/seata-nacos-analysis.html)安装seata跟nacos配置

## 测试

- 先后启动provider跟comsumer
- 测试mp分页查询是否启用: http://127.0.0.1:28888/test/pageByProduct
- 测试事务回滚: http://127.0.0.1:28888/test/testRollback
- 测试事务提交: http://127.0.0.1:28888/test/testCommit

