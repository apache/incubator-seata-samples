package com.sever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"io.seata"})
public class ServerApplication {
    public static void main(String[] args) throws IOException {
        // run the spring-boot application
        SpringApplication.run(com.sever.ServerApplication.class, args);
    }
}