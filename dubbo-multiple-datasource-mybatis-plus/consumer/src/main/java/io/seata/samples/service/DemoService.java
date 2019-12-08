package io.seata.samples.service;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.samples.entity.Account;
import io.seata.samples.entity.Orders;
import io.seata.samples.entity.Product;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;

/**
 * 
 * @author 陈健斌
 * @date 2019/12/05
 */
@Service
public class DemoService {
    @Reference(version = "1.0.0", timeout = 60000)
    private IAccountService accountService;
    @Reference(version = "1.0.0", timeout = 60000)
    private IOrdersService ordersService;
    @Reference(version = "1.0.0", timeout = 60000)
    private IProductService productService;
    private final static Logger logger = LoggerFactory.getLogger(DemoService.class);
    private Lock lock = new ReentrantLock();

    /**
     * 
     * @return
     * @throws TransactionException
     */
    @GlobalTransactional
    public Object One() throws TransactionException {
        logger.info("seata分布式事务Id:{}", RootContext.getXID());
        try {
            lock.lock();
            LocalDateTime now = LocalDateTime.now();
            Product product = productService.getById(1);
            Account account = accountService.getById(1);
            Orders orders = new Orders();
            orders.setCreateTime(now);
            orders.setProductId(product.getId());
            orders.setReplaceTime(now);
            orders.setSum(1);
            orders.setAmount(product.getPrice());
            orders.setAccountId(account.getId());
            product.setStock(product.getStock() - 1);
            account.setSum(account.getSum() != null ? account.getSum() + 1 : 1);
            account.setLastUpdateTime(now);
            productService.updateById(product);
            accountService.updateById(account);
            ordersService.save(orders);
            int i = 1 / 0;
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            logger.info("载入事务id进行回滚");
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 
     * @return
     * @throws TransactionException
     */
    @GlobalTransactional
    public Object Two() throws TransactionException {
        logger.info("seata分布式事务Id:{}", RootContext.getXID());
        try {
            lock.lock();
            LocalDateTime now = LocalDateTime.now();
            Product product = productService.getById(1);
            Account account = accountService.getById(1);
            Orders orders = new Orders();
            orders.setCreateTime(now);
            orders.setProductId(product.getId());
            orders.setReplaceTime(now);
            orders.setSum(1);
            orders.setAmount(product.getPrice());
            orders.setAccountId(account.getId());
            product.setStock(product.getStock() - 1);
            account.setSum(account.getSum() != null ? account.getSum() + 1 : 1);
            account.setLastUpdateTime(now);
            productService.updateById(product);
            accountService.updateById(account);
            ordersService.save(orders);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            logger.info("载入事务id进行回滚");
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            return false;
        } finally {
            lock.unlock();
        }
    }
}
