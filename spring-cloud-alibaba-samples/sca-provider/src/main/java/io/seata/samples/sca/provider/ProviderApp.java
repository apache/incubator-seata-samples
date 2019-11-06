package io.seata.samples.sca.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by yu.hb on 2019-10-30
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("io.seata.samples.sca.provider.mapper")
public class ProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class, args);
    }
}
