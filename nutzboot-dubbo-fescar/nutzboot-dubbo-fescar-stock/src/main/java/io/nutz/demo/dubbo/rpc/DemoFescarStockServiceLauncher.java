package io.nutz.demo.dubbo.rpc;

import io.nutz.demo.bean.Stock;
import org.nutz.boot.NbApp;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(create = "init")
public class DemoFescarStockServiceLauncher {

    @Inject
    Dao dao;

    public void init() {
        dao.create(Stock.class, false);
        if (dao.count(Stock.class, Cnd.where("commodityCode", "=", "C00321")) == 0) {
            Stock stock = new Stock();
            stock.setCommodityCode("C00321");
            stock.setCount(100);
            dao.insert(stock);
        }
    }

    public static void main(String[] args) throws Exception {
        new NbApp().run();
    }

}
