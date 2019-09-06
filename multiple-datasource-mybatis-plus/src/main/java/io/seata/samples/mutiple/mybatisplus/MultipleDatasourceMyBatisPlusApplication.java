package io.seata.samples.mutiple.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HelloWoodes
 */
@SpringBootApplication
@MapperScan("io.seata.samples.mutiple.mybatisplus.dao")
public class MultipleDatasourceMyBatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultipleDatasourceMyBatisPlusApplication.class, args);
    }

}
