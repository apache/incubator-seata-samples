package io.seata.sample.service;

import io.seata.sample.entity.Stock;
import io.seata.sample.repository.StockDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-04
 */
@Service
public class StockService {

    @Autowired
    private StockDAO stockDAO;

    @Transactional
    public void deduct(String commodityCode, int count) {
        Stock stock = stockDAO.findByCommodityCode(commodityCode);
        stock.setCount(stock.getCount() - count);

        stockDAO.save(stock);
    }
}
