package io.nutz.demo.dubbo.rpc.service.impl;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fescar.core.context.RootContext;

import io.nutz.demo.bean.Account;
import io.nutz.demo.dubbo.rpc.service.AccountService;

@IocBean
@Service(interfaceClass=AccountService.class)
public class AccountServiceImpl implements AccountService {

    private static final Log log = Logs.get();

    @Inject
    private Dao dao;

    @Override
    public void debit(String userId, int money) {
        log.info("Account Service ... xid: " + RootContext.getXID());
        log.infof("Deducting balance SQL: update account_tbl set money = money - %s where user_id = %s", money, userId);

        dao.update(Account.class, Chain.makeSpecial("money", "-" + money), Cnd.where("userId", "=", userId));
        log.info("Account Service End ... ");
    }
}