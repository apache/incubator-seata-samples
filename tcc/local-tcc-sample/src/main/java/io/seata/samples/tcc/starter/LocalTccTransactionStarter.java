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
package io.seata.samples.tcc.starter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import io.seata.common.util.StringUtils;
import io.seata.samples.tcc.ApplicationKeeper;
import io.seata.samples.tcc.action.ResultHolder;
import io.seata.samples.tcc.action.impl.TccActionOneImpl;
import io.seata.samples.tcc.action.impl.TccActionTwoImpl;
import io.seata.samples.tcc.service.TccTransactionService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * The type Local tcc transaction starter.
 *
 * @author zhangsen
 */
public class LocalTccTransactionStarter {

    /**
     * The Application context.
     */
    static AbstractApplicationContext applicationContext = null;

    /**
     * The Tcc transaction service.
     */
    static TccTransactionService tccTransactionService = null;

    /**
     * The Tcc action one.
     */
    static TccActionOneImpl tccActionOne = null;

    /**
     * The Tcc action two.
     */
    static TccActionTwoImpl tccActionTwo = null;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException, SQLException {
        applicationContext = new ClassPathXmlApplicationContext(new String[] {"spring/seata-tcc.xml","db/provider-commom-fence-datasource.xml"});

        tccTransactionService = (TccTransactionService)applicationContext.getBean("tccTransactionService");

        tccActionOne = (TccActionOneImpl)applicationContext.getBean("tccActionOne");
        tccActionTwo = (TccActionTwoImpl)applicationContext.getBean("tccActionTwo");

        // create fence log table
        DataSource dataSource = (DataSource) applicationContext.getBean("seataTCCFenceDataSource");
        createFenceLogTableSql(dataSource);

        //分布式事务提交demo
        transactionCommitDemo();

        //分布式事务回滚demo
        transactionRollbackDemo();

        new ApplicationKeeper(applicationContext).keep();
    }

    private static void transactionCommitDemo() throws InterruptedException {
        String txId = tccTransactionService.doTransactionCommit();
        System.out.println(txId);
        Assert.isTrue(StringUtils.isNotBlank(txId), "事务开启失败");

        Thread.sleep(1000L);

        Assert.isTrue("T".equals(ResultHolder.getActionOneResult(txId)), "tccActionOne commit failed");
        Assert.isTrue("T".equals(ResultHolder.getActionTwoResult(txId)), "tccActionTwo commit failed");

        System.out.println("transaction commit demo finish.");
    }

    private static void transactionRollbackDemo() throws InterruptedException {
        Map map = new HashMap(16);
        try {
            tccTransactionService.doTransactionRollback(map);
            Assert.isTrue(false, "分布式事务未回滚");
        } catch (Throwable t) {
            Assert.isTrue(true, "分布式事务异常回滚");
        }
        String txId = (String)map.get("xid");
        Thread.sleep(1000L);

        Assert.isTrue("R".equals(ResultHolder.getActionOneResult(txId)), "tccActionOne commit failed");
        Assert.isTrue("R".equals(ResultHolder.getActionTwoResult(txId)), "tccActionTwo commit failed");

        System.out.println("transaction rollback demo finish.");
    }

    private static void createFenceLogTableSql(DataSource dataSource) throws SQLException {
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

}
