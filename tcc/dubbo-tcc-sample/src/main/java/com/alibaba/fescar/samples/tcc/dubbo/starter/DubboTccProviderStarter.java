package com.alibaba.fescar.samples.tcc.dubbo.starter;

import com.alibaba.fescar.common.util.StringUtils;
import com.alibaba.fescar.samples.tcc.dubbo.ApplicationKeeper;
import com.alibaba.fescar.samples.tcc.dubbo.action.ResultHolder;
import com.alibaba.fescar.samples.tcc.dubbo.service.TccTransactionService;
import org.apache.curator.test.TestingServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zhangsen
 */
public class DubboTccProviderStarter {

    private static TestingServer server;

    public static void main(String[] args) throws Exception {
        //mock zk server
        mockZKServer();

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[] {"spring/fescar-tcc.xml", "spring/fescar-dubbo-provider.xml"});

        new ApplicationKeeper(applicationContext).keep();
    }

    private static void mockZKServer() throws Exception {
        //Mock zk server，作为 dubbo 配置中心
        server = new TestingServer(2181, true);
        server.start();
    }

}
