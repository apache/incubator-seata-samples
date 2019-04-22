package io.seata.samples.tcc.dubbo.starter;

import io.seata.common.util.StringUtils;
import io.seata.samples.tcc.dubbo.ApplicationKeeper;
import io.seata.samples.tcc.dubbo.action.ResultHolder;
import io.seata.samples.tcc.dubbo.service.TccTransactionService;
import org.apache.curator.test.TestingServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Dubbo tcc provider starter.
 *
 * @author zhangsen
 */
public class DubboTccProviderStarter {

    private static TestingServer server;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        //mock zk server
        mockZKServer();

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[] {"spring/seata-tcc.xml", "spring/seata-dubbo-provider.xml"});

        new ApplicationKeeper(applicationContext).keep();
    }

    private static void mockZKServer() throws Exception {
        //Mock zk server，作为 dubbo 配置中心
        server = new TestingServer(2181, true);
        server.start();
    }

}
