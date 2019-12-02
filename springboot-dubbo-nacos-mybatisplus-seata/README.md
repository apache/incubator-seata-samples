# springboot-dubbo-mybatsiplus-seata

#### 介绍
springboot-dubbo-mybatsiplus-seata  demo整合,欢迎大家尝试,目前seata的文档跟博客较少,后续我会根据这个项目发一篇博客,再过一阵子seata 正式版要出了,欢迎大家去seata的官网http://seata.io/zh-cn/ 学习

#### 软件架构
本搭建教程详细已经发布在csdn:
搭建直连方式教程:https://blog.csdn.net/qq_35721287/article/details/103232506

nacos方式连接教程:https://blog.csdn.net/qq_35721287/article/details/103264621

修复mp整合nacos被代理数据源导致mp特性丧失的bug:https://blog.csdn.net/qq_35721287/article/details/103282589


#### 安装教程

1.  链接: https://pan.baidu.com/s/1D3etVX6ijx8m0ffGltq1Kw 提取码: kubd 复制这段内容后打开百度网盘手机App，操作更方便哦 下载seata服务
2.  mysql建表建库,sql已在sql文件夹内,自行建库,运行sql代码
3.  seata文件夹内conf文件夹内的file.conf文件夹记事本打开找到 database store,配置db为自己的数据库地址账号密码
4.	cd bin .\seata-service.bat 查看运行状态

#### 使用说明

1.  拉取本项目后,先运行test-service后运行test-client,访问ip:端口/swagger-ui.html 内直接test即可查看分布式事务回滚情况
	自行可修改org.test.service.DemoService下的事务回滚代码测试不同情况
2.  test-service内的org.test.SeataAutoConfig.java内必须先代理数据源,后初始化seata事务
	第一个填的服务名,第二个是事务分组,事务分组比如叫:test-group,那么resources内的file.conf内的vgroup_mapping.XXXX,XXXX为test-group
	client内的事务组建于与service一样,保障回滚正常
3.  resources内的file.conf打开后service内的ip跟地址需要改为seata服务的ip跟地址,比如:127.0.0.1:8091

