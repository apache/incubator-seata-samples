package io.seata.sample.service;

import io.seata.sample.dao.OrderDao;
import io.seata.sample.entity.Order;
import io.seata.sample.feign.AccountApi;
import io.seata.sample.feign.StorageApi;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author IT云清
 */
@Service("orderServiceImpl")
public class OrderServiceImpl implements OrderService{

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private StorageApi storageApi;
    @Autowired
    private AccountApi accountApi;

    /**
     * 创建订单
     * @param order
     * @return
     * 测试结果：
     * 1.添加本地事务：仅仅扣减库存
     * 2.不添加本地事务：创建订单，扣减库存
     */
    @Override
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        LOGGER.info("------->交易开始");
        //本地方法
        orderDao.create(order);

        //远程方法 扣减库存
        storageApi.decrease(order.getProductId(),order.getCount());

        //远程方法 扣减账户余额

        LOGGER.info("------->扣减账户开始order中");
        accountApi.decrease(order.getUserId(),order.getMoney());
        LOGGER.info("------->扣减账户结束order中");

        LOGGER.info("------->交易结束");
    }
}
