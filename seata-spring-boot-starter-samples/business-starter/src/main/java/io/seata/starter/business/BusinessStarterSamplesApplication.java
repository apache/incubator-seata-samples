package io.seata.starter.business;

import com.alibaba.cloud.seata.GlobalTransactionAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
  *@author lkx_soul
 * @create 2019.12.23
  **/
@SpringBootApplication(exclude = GlobalTransactionAutoConfiguration.class)
public class BusinessStarterSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessStarterSamplesApplication.class);
    }

}
