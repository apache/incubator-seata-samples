package io.nutz.demo.dubbo.rpc;

import org.nutz.boot.NbApp;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import io.nutz.demo.bean.Account;

@IocBean(create="init")
public class DemoFescarAccountServiceLauncher {

    @Inject
    Dao dao;
    
    public void init() {
        dao.create(Account.class, false);
        if (dao.count(Account.class, Cnd.where("userId", "=", "U100001")) == 0) {
            Account account = new Account();
            account.setUserId("U100001");
            account.setMoney(1000);
            dao.insert(account);
        }
    }
    
    public static void main(String[] args) throws Exception {
        new NbApp().run();
    }

}
