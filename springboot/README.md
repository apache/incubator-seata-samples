# springboot

Integrated seata into springboot+jpa+dubbo+druid project.


in this demo, we simulate a deposit service, there will be two tables, one is for deposit record, the other is the  balance of account asset. 

when we invoke increaseAmount, It will write a deposit record and then invoke a remote service called assignService

1. install a mysql server

2. connect to your db server, and execute [sql/init_db.sql](https://github.com/seata/seata-samples/blob/master/springboot/src/main/resources/sql/initial_db.sql)  to init the table

3. Configure the Db instance address in [/src/main/resources/application.yml](https://github.com/seata/seata-samples/blob/master/springboot/src/main/resources/application.yml#L13-L35), 
 including database name, username and password
 
4. configure the spring.dubbo.registry in application.yml if necessary,if multicast is not ok, for example, you can set it as `zookeeper://localhost:2181`

5. start up your seata server, which means the tc role 

6. Startup the SeataSpringbootApp .

7. visit http://127.0.0.1:9999/demo//asset/assign , and you will see an exception, which throw by remote service, you can check your asset table, ps: you can vist many times as you wish.
