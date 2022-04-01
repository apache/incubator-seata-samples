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
package io.nutz.demo.dubbo.rpc;

import io.nutz.demo.bean.Account;
import org.nutz.boot.NbApp;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(create = "init")
public class DemoSeataAccountServiceLauncher {

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
