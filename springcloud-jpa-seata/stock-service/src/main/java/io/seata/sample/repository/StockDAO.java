package io.seata.sample.repository;

import io.seata.sample.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-04
 */
public interface StockDAO extends JpaRepository<Stock, String> {

    Stock findByCommodityCode(String commodityCode);

}
