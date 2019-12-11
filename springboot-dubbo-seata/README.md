# SpringBoot + Dubbo + Mybatis + Nacos + Seata

Integration SpringBoot + Dubbo + Mybatis + Nacos + Seata


### 1. clone the code 
  
   - samples-common  common module
       
   - samples-account  user account module
     
   - samples-order  order module
   
   - samples-storage  storage module

   - samples-business  business module

### 2. prepare database 

create database （默认为：seata），import db_seata.sql to database 

then you will see ：

```
+-------------------------+
| Tables_in_seata         |
+-------------------------+
| t_account               |
| t_order                 |
| t_storage               |
| undo_log                |
+-------------------------+
```

### 3. start Nacos（使用1.1.0版本，防止因为dubbo，nacos因版本不匹配出现的心跳请求出错的情况。 v1.1.0地址：https://github.com/alibaba/nacos/releases/tag/1.1.0）

Nacos quickstart：https://nacos.io/en-us/docs/quick-start.html

enter the  Nacos webconsole：http://127.0.0.1:8848/nacos/index.html
   
### 4. start Seata Server
  
download page：https://github.com/seata/seata/releases

download and unzip seata-server，cd the bin dictory, and run 

```bash
sh seata-server.sh 8091 file
```

### 5. start the demo module

start samples-account、samples-order、samples-storage、samples-business

use Nacos webconsole to ensure the registry is ok: http://127.0.0.1:8848/nacos/#/serviceManagement

> check the datasource config in application.properties is right.
    
### 6. start the normal request

use postman to send a post request：http://localhost:8104/business/dubbo/buy  

body：

```json
{
    "userId":"1",
    "commodityCode":"C201901140001",
    "name":"fan",
    "count":2,
    "amount":"100"
}
```

or use curl：

```bash
curl -H "Content-Type:application/json" -X POST -d '{"userId":"1","commodityCode":"C201901140001","name":"风扇","count":2,"amount":"100"}' localhost:8104/business/dubbo/buy
``` 

then this will send a pay request,and return code is 200

### 7. test the rollback request

enter samples-business , change  BusinessServiceImpl, uncomment the following code ：

```
if (!flag) {
  throw new RuntimeException("测试抛异常后，分布式事务回滚！");
}
```

restart the  samples-business module, and execute the step 6.
