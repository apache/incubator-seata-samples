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
package io.seata.samples.tcc.dubbo.starter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import io.seata.samples.jit.AbstractStarter;
import io.seata.samples.jit.ApplicationKeeper;
import org.apache.curator.test.TestingServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The type Dubbo tcc provider starter.
 *
 * @author zhangsen
 * @author ppf
 */
public class TccProviderStarter extends AbstractStarter {

    private static TestingServer server;
    
    public static void main(String[] args) throws Exception {
        new TccProviderStarter().start0(args);
    }

    @Override
    protected void start0(String[] args) throws Exception {
        //mock zk server
        mockZKServer();
        
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
            new String[] {"spring/seata-tcc-provider.xml", "spring/seata-dubbo-provider.xml", "db/provider-commom-fence-datasource.xml"});

        // create fence log table
        DataSource dataSource = (DataSource) applicationContext.getBean("seataTCCFenceDataSource");
        this.createFenceLogTableSql(dataSource);

        new ApplicationKeeper().keep();
    }

    private void createFenceLogTableSql(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `tcc_fence_log`\n" +
                "(\n" +
                "    `xid`           VARCHAR(128)  NOT NULL COMMENT 'global id',\n" +
                "    `branch_id`     BIGINT        NOT NULL COMMENT 'branch id',\n" +
                "    `action_name`   VARCHAR(64)   NOT NULL COMMENT 'action name',\n" +
                "    `status`        TINYINT       NOT NULL COMMENT 'status(tried:1;committed:2;rollbacked:3;suspended:4)',\n" +
                "    `gmt_create`    DATETIME(3)   NOT NULL COMMENT 'create time',\n" +
                "    `gmt_modified`  DATETIME(3)   NOT NULL COMMENT 'update time',\n" +
                "    PRIMARY KEY (`xid`, `branch_id`),\n" +
                "    KEY `idx_gmt_modified` (`gmt_modified`),\n" +
                "    KEY `idx_status` (`status`)\n" +
                ") ;");
        stmt.close();
        connection.close();
    }

    private static void mockZKServer() throws Exception {
        //Mock zk server，作为 transfer 配置中心
        server = new TestingServer(2181, true);
        server.start();
    }
}
