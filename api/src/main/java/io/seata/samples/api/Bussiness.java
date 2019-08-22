package io.seata.samples.api;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import io.seata.core.exception.TransactionException;
import io.seata.rm.RMClient;
import io.seata.samples.api.service.AccountService;
import io.seata.samples.api.service.OrderService;
import io.seata.samples.api.service.StorageService;
import io.seata.samples.api.service.impl.AccountServiceImpl;
import io.seata.samples.api.service.impl.OrderServiceImpl;
import io.seata.samples.api.service.impl.StorageServiceImpl;
import io.seata.tm.TMClient;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;

/**
 * The type Bussiness.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21 使用api 构建 单体应用多数据源分布式事务 非spring环境
 */
public class Bussiness {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     * @throws TransactionException the transaction exception
     */
    public static void main(String[] args) throws SQLException, TransactionException, InterruptedException {

        String userId = "U100001";
        String commodityCode = "C00321";
        int commodityCount = 100;
        int money = 999;
        AccountService accountService = new AccountServiceImpl();
        StorageService storageService = new StorageServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        orderService.setAccountService(accountService);

        //reset data
        accountService.reset(userId, String.valueOf(money));
        storageService.reset(commodityCode, String.valueOf(commodityCount));
        orderService.reset(null, null);

        //init seata; only once
        String applicationId = "api";
        String txServiceGroup = "my_test_tx_group";
        TMClient.init(applicationId, txServiceGroup);
        RMClient.init(applicationId, txServiceGroup);

        //trx
        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
        try {
            tx.begin(60000, "testBiz");
            System.out.println("begin trx, xid is " + tx.getXid());

            //biz operate 3 dataSources
            //set >=5 will be rollback(200*5>999) else will be commit
            int opCount = 5;
            storageService.deduct(commodityCode, opCount);
            orderService.create(userId, commodityCode, opCount);

            //check data if negative
            boolean needCommit = ((StorageServiceImpl)storageService).validNegativeCheck("count", commodityCode)
                && ((AccountServiceImpl)accountService).validNegativeCheck("money", userId);

            //if data negative rollback else commit
            if (needCommit) {
                tx.commit();
            } else {
                System.out.println("rollback trx, cause: data negative, xid is " + tx.getXid());
                tx.rollback();
            }
        } catch (Exception exx) {
            System.out.println("rollback trx, cause: " + exx.getMessage() + " , xid is " + tx.getXid());
            tx.rollback();
            throw exx;
        }
        TimeUnit.SECONDS.sleep(10);

    }
}
