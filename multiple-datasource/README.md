# Spring Cloud 使用 Seata 实现分布式事务 - 多数据源

> 使用 Seata 作为分布式事务组件，使用 MySQL 数据库，使用 MyBatis 作为数据访问层实现多数据源下事务一致

## 环境准备

### 创建数据库及表

执行 `data.sql`

### 启动 Seata Server 

可以直接通过bash 脚本启动 Seata Server，也可以通过 Docker 镜像启动，但是 Docker 方式目前只支持使用 file 模式，不支持将 Seata-Server 注册到 Eureka 或 Nacos 等注册中心

- 通过脚本启动

在 [Seata Release](https://github.com/seata/seata/releases) 下载相应版本的 Seata Server，解压后执行以下命令启动，这里使用 file 配置

```bash
sh ./bin/seata-server.sh -p 8091 -m file
```

- 通过 Docker 启动

```bash
docker run --name seata-file -d -p 8091:8091 hellowoodes/seata:0.7.1-file
```

## 测试

- 启动应用

- 测试成功场景

调用 placeOrder 接口，将 price 设置为 1，此时余额为 10，可以下单成功

```bash
curl -X POST \
  http://localhost:8081/order/placeOrder \
  -H 'Content-Type: application/json' \
  -d '{
    "userId": 1,
    "productId": 1,
    "price": 1
}'
```

此时返回结果为：

```json
{"success":true,"message":null,"data":null}
```

- 测试失败场景

设置 price 为 100，此时余额不足，会下单失败，pay-service会抛出异常，事务会回滚

```bash
curl -X POST \
  http://localhost:8081/order/placeOrder \
  -H 'Content-Type: application/json' \
  -d '{
    "userId": 1,
    "productId": 1,
    "price": 100
}'
```

查看 undo_log 的日志或者主键，可以看到在执行过程中有保存数据

如查看主键自增的值，在执行前后的值会发生变化，在执行前是 1，执行后是 5

```sql
SELECT
    auto_increment
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'seata_order'
  AND TABLE_NAME = 'undo_log'
```