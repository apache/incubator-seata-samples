package com.seata.inventory.mapper;

import com.seata.inventory.model.Inventory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository
public interface InventoryMapper  extends Mapper<Inventory> {

    @Update("update inventory_tbl set count = count - #{count} where commodity_code = #{commodityCode}")
    void occupy(@Param("commodityCode") String commodityCode , @Param("count") int count);

    @Update("update inventory_tbl set count = count + #{count} where commodity_code = #{commodityCode}")
    void rollBackInventory(@Param("commodityCode") String commodityCode , @Param("count") int count);
}
