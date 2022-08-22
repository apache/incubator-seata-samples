package com.seata.business.service.impl;

import com.seata.business.client.AccountClient;
import com.seata.business.client.InventoryClient;
import com.seata.business.client.OrderClient;
import com.seata.business.model.Order;
import com.seata.business.service.BusinessService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private OrderClient orderClient;
    @Autowired
    private InventoryClient inventoryClient;
    @Autowired
    private AccountClient accountClient;



    @Override
    public String prepareBuy(String userId, String commodityCode, int count) {
        log.info("开始TCC xid:" + RootContext.getXID());
//        使用BusinessActionContextUtil工具类可将一阶段参数传递给二阶段，也可使用@BusinessActionContextParameter绑定参数，二者选其一
//        BusinessActionContextUtil.addContext("userId",userId);
//        BusinessActionContextUtil.addContext("commodityCode",commodityCode);
//        BusinessActionContextUtil.addContext("count",count);
        //1.查询账户 预占扣款
        int money = count * 1;
        accountClient.occupy(userId,money);
        //2.远程创建订单
        Order order  = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(money);
        orderClient.create(order);
        //3.远程预占库存
        inventoryClient.occupy(commodityCode,count);
        return "success";
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        log.info("xid = " + actionContext.getXid() + "提交成功");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        // 获取下单时的提交参数
        String userId = String.valueOf(actionContext.getActionContext("userId"));
        int count = Integer.parseInt(String.valueOf(actionContext.getActionContext("count")));
        String commodityCode = String.valueOf(actionContext.getActionContext("commodityCode"));
        // 进行分支事务账户预占的金额和仓库预占的库存回滚
        accountClient.rollBackAccount(userId,count);
        inventoryClient.rollBackInventory(commodityCode,count);
        log.info("xid = " + actionContext.getXid() + "进行回滚操作");
        return true;
    }


}
