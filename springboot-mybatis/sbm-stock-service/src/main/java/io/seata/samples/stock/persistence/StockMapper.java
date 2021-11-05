package io.seata.samples.stock.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockMapper {

    Stock selectById(@Param("id") Integer id);

    Stock findByCommodityCode(@Param("commodityCode") String commodityCode);

    int updateById(Stock record);

    void insert(Stock record);

    void insertBatch(List<Stock> records);

    int updateBatch(@Param("list") List<Long> ids, @Param("commodityCode") String commodityCode);
}