package com.alibaba.fescar.samples.dubbo.starter;

import com.alibaba.fescar.samples.dubbo.ApplicationKeeper;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class DubboStorageServiceStarter {
    /**
     * 1. Storage service is ready . A seller add 100 storage to a sku: C00321
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext storageContext = new ClassPathXmlApplicationContext(
                new String[]{"spring/dubbo-storage-service.xml"});
        storageContext.getBean("service");
        JdbcTemplate storageJdbcTemplate = (JdbcTemplate) storageContext.getBean("jdbcTemplate");
        storageJdbcTemplate.update("delete from storage_tbl where commodity_code = 'C00321'");
        storageJdbcTemplate.update("insert into storage_tbl(commodity_code, count) values ('C00321', 100)");
        new ApplicationKeeper(storageContext).keep();
    }
}
