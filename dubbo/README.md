# dubbo

How to use Seata to ensure consistency between Dubbo Microservices ？  

Please see the blog:   
Engligh: http://dubbo.apache.org/en-us/blog/dubbo-fescar.html   
中   文: http://dubbo.apache.org/zh-cn/blog/dubbo-fescar.html

## Quick Start 

1. Startup Seata Server 

```shell script
docker run --name seata-file -p 8091:8091 hellowoodes/seata:0.9.0-file
```
> You can also download seata [release](https://github.com/seata/seata/releases) version ,then run bin/seata-server .

2. Startup Zookeeper 

```shell script
docker run --name zookeeper -p 2181:2181 -p 2888:2888 -p 3888:3888 -d zookeeper
```

3. Execute SQL under `seata-samples\dubbo\src\main\resources\sql`

4. Run `DubboStorageServiceStarter`,`DubboAccountServiceStarter`,`DubboOrderServiceStarter`,`DubboBusinessTester`

