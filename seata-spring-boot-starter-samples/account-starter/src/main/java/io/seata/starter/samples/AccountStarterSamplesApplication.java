package io.seata.starter.samples;

import com.alibaba.cloud.seata.GlobalTransactionAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
  *@author lkx_soul
 * @create 2019.12.23
  **/
@SpringBootApplication(exclude = GlobalTransactionAutoConfiguration.class)
public class AccountStarterSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountStarterSamplesApplication.class);
    }

}
