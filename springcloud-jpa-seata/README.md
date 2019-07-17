# Seata Spring Cloud Sample

Spring Cloud 中使用 Seata，使用 Feign 实现远程调用，使用 Spring Jpa 访问 MySQL 数据库

### 准备工作

1. 执行`sql/all_in_one.sql`

2. 下载最新版本的 [Seata Sever](https://github.com/seata/seata/releases)

3. 解压并启动 Seata server

```bash
unzip seata-server-xxx.zip

cd distribution
sh ./bin/seata-server.sh 8091 file
```

4. 启动 Account, Order, Storage, Business 服务

> 数据库配置的用户名和密码是 `root`和`123456`，因为没有使用注册中心，所有的 Feign 的配置都是 `127.0.0.1+端口`，如果不同请手动修改

### 测试 
 
- 无错误成功提交

```bash
curl http://127.0.0.1:8084/purchase/commit
``` 
完成后可以看到数据库中 `account_tbl`的`id`为1的`money`会减少 5，`order_tbl`中会新增一条记录，`storage_tbl`的`id`为1的`count`字段减少 1

- 发生异常事务回滚

```bash
curl http://127.0.0.1:8084/purchase/rollback
```
此时 account-service 会抛出异常，发生回滚，待完成后数据库中的数据没有发生变化，回滚成功

### 注意

- 注入 DataSourceProxy 

因为 Seata 通过代理数据源实现分支事务，如果没有注入，事务无法成功回滚

```java
@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 需要将 DataSourceProxy 设置为主数据源，否则事务无法回滚
     *
     * @param druidDataSource The DruidDataSource
     * @return The default datasource
     */
    @Primary
    @Bean("dataSource")
    public DataSource dataSource(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }
}
```

- file.conf 的 service.vgroup_mapping 配置必须和`spring.application.name`一致

在 `org.springframework.cloud:spring-cloud-starter-alibaba-seata`的`org.springframework.cloud.alibaba.seata.GlobalTransactionAutoConfiguration`类中，默认会使用 `${spring.application.name}-fescar-service-group`作为服务名注册到 Seata Server上，如果和`file.conf`中的配置不一致，会提示 `no available server to connect`错误

也可以通过配置 `spring.cloud.alibaba.seata.tx-service-group`修改后缀，但是必须和`file.conf`中的配置保持一致