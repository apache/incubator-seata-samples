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
package io.seata.samples.service;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.samples.entity.Account;
import io.seata.samples.entity.Orders;
import io.seata.samples.entity.Product;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import org.springframework.stereotype.Service;

/**
 * @author 陈健斌
 * @date 2019/12/05
 */
@Service
public class DemoServiceImpl implements DemoService {
    private final static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
    @DubboReference(version = "1.0.0", timeout = 60000, check = false)
    private IAccountService accountService;
    @DubboReference(version = "1.0.0", timeout = 60000, check = false)
    private IOrdersService ordersService;
    @DubboReference(version = "1.0.0", timeout = 60000, check = false)
    private IProductService productService;
    private Lock lock = new ReentrantLock();

    /**
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
