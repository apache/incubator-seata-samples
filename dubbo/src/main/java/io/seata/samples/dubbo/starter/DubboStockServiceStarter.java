/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.samples.dubbo.starter;

import io.seata.samples.dubbo.ApplicationKeeper;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The type Dubbo stock service starter.
 */
public class DubboStockServiceStarter {
    /**
     * 1. Stock service is ready . A seller add 100 stock to a sku: C00321
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext stockContext = new ClassPathXmlApplicationContext(
            new String[] {"spring/dubbo-stock-service.xml"});
        stockContext.getBean("service");
        JdbcTemplate stockJdbcTemplate = (JdbcTemplate)stockContext.getBean("jdbcTemplate");
        stockJdbcTemplate.update("delete from stock_tbl where commodity_code = 'C00321'");
        stockJdbcTemplate.update("insert into stock_tbl(commodity_code, count) values ('C00321', 100)");
        new ApplicationKeeper(stockContext).keep();
    }
}
