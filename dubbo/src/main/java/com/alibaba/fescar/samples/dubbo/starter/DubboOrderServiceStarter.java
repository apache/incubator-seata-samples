package com.alibaba.fescar.samples.dubbo.starter;

import com.alibaba.fescar.samples.dubbo.ApplicationKeeper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboOrderServiceStarter {
    public static void main(String[] args) {
        /**
         *  3. Order service is ready . Waiting for buyers to order
         */
        ClassPathXmlApplicationContext orderContext = new ClassPathXmlApplicationContext(new String[]{"spring/dubbo-order-service.xml"});
        orderContext.getBean("service");
        new ApplicationKeeper(orderContext).keep();
    }
}
