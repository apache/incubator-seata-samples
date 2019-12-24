## seata-spring-boot-starter-samples

### 前沿

- 如果你的项目中使用的是springBoot类型，可以直接添加如下依赖

		<dependency>
			<groupId>io.seata</groupId>
			<artifactId>seata-spring-boot-starter</artifactId>
			<version>1.0.0</version>
		</dependency>

- 特性

      可以将客户端的配置文件放在application/yml管理，更加方便、简洁

### 环境准备

#### 创建数据库及表

 执行 seata_server1.sql、seata_server2.sql

#### 启动 Seata server

- 通过脚本启动

	在 [Seata Release ](https://github.com/seata/seata/releases "Seata Release ")下载相应版本的 Seata Server，解压后执行 seata-server.bat  命令启动，这里使用 file 配置

#### 测试

- 启动应用 account-starter
- 启动应用 business-starter
- 测试场景

	调用 insertInfo 接口，传入参数商品数量(count),商品价格(money),是否回滚(isBack)
- 测试一

        curl -X GET \
          http://localhost:8888/insertInfo?count=5&money=50&isBack=false \
          -H 'Content-Type: application/json' \
    
    
   此时返回结果为：二阶段提交
    
   查看数据库中的数据，商品表添加数据，库存表修改商品数据

- 测试二

         curl -X GET \
          http://localhost:8888/insertInfo?count=5&money=50&isBack=true \
          -H 'Content-Type: application/json' \
    
         
   此时抛出异常，执行回滚，查看日志 Branch Rollbacked result: PhaseTwo_Rollbacked
    
   查看数据库中的数据，商品表无数据变更，库存表无数据变更
    
