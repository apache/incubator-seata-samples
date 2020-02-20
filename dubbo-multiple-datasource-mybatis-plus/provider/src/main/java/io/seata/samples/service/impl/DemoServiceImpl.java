package io.seata.samples.service.impl;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.samples.entity.Account;
import io.seata.samples.entity.Orders;
import io.seata.samples.entity.Product;
import io.seata.samples.service.DemoService;
import io.seata.samples.service.IAccountService;
import io.seata.samples.service.IOrdersService;
import io.seata.samples.service.IProductService;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;

/**
 *
 * @author 陈健斌
 * @date 2019/12/05
 */
@Service(version = "1.0.0", interfaceClass = DemoService.class, timeout = 60000)
public class DemoServiceImpl implements DemoService {
    private final static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IProductService productService;
    private Lock lock = new ReentrantLock();

    /**
     *
     * @return
     * @throws TransactionException
     */
    @Override
    @GlobalTransactional
    public Object testRollback() throws TransactionException {
        logger.info("seata分布式事务Id:{}", RootContext.getXID());
        lock.lock();
        try {
            LocalDateTime now = LocalDateTime.now();
            Product product = productService.getOne(Wrappers.<Product>query().eq("id", 1).last("for update"));
            if (product.getStock() > 0) {
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
            } else {
                return false;
            }
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
    @Override
    @GlobalTransactional
    public Object testCommit() throws TransactionException {
        lock.lock();
        try {
            LocalDateTime now = LocalDateTime.now();
            Product product = productService.getOne(Wrappers.<Product>query().eq("id", 1).last("for update"));
            if (product.getStock() > 0) {
                logger.info("seata分布式事务Id:{}", RootContext.getXID());
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
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.info("载入事务{}进行回滚" + e.getMessage(), RootContext.getXID());
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            return false;
        } finally {
            lock.unlock();
        }
    }
}
