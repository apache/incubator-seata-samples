package com.alibaba.fescar.samples.dubbo.starter;

import com.alibaba.fescar.samples.dubbo.service.BusinessService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboBusinessTester {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        /**
         *  4. The whole e-commerce platform is ready , The buyer(U100001) create an order on the sku(C00321) , the count is 2
         */
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"spring/dubbo-business.xml"});
        final BusinessService business = (BusinessService) context.getBean("business");
        business.purchase("U100001", "C00321", 2);
    }
}
