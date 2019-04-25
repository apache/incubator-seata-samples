# seata-sample
基于spring cloud+feign+spring jpa+spring cloud alibaba seata+mysql

### 准备工作
1. 执行sql/all_in_one.sql

2. 下载[0.5.0](https://github.com/seata/seata/releases/tag/0.5.0)版本server

   客户端与服务端版本号保持一致
3. 启动seata server

   sh seata-server.sh 8091 ../data/
4. 启动business、storage、account、order

   数据库默认连接127.0.0.1:3306，不同的注意修改

5. 事务成功 GET http://127.0.0.1:8084/purchase/commit

6. 事务回滚 GET http://127.0.0.1:8084/purchase/rollback

### 验证数据
1. 事务成功

   库存减1、订单加1、余额减5
2. 事务回滚

   数据无变化