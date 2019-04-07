package io.nutz.demo.dubbo.rpc;

import org.nutz.boot.NbApp;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import io.nutz.demo.bean.Storage;

@IocBean(create="init")
public class DemoFescarStorageServiceLauncher {
    
@Inject Dao dao;
    
    public void init() {
        dao.create(Storage.class, false);
        if (dao.count(Storage.class, Cnd.where("commodityCode", "=", "C00321")) == 0) {
            Storage storage = new Storage();
            storage.setCommodityCode("C00321");
            storage.setCount(100);
            dao.insert(storage);
        }
    }

    public static void main(String[] args) throws Exception {
        new NbApp().run();
    }

}
