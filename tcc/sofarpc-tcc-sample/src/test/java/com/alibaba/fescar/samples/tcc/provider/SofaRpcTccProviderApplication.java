package com.alibaba.fescar.samples.tcc.provider;

import org.apache.curator.test.TestingServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 *
 * @author zhangsen
 */
@SpringBootApplication
@ImportResource("classpath:provider/*.xml")
public class SofaRpcTccProviderApplication {

    private static TestingServer server;

    public static void main(String[] args) throws Exception {
        //mock zk server
        mockZKServer();

        ApplicationContext applicationContext = SpringApplication.run(SofaRpcTccProviderApplication.class, args);

    }

    private static void mockZKServer() throws Exception {
        //Mock zk server，作为  配置中心
        server = new TestingServer(2181, true);
        server.start();
    }

}
