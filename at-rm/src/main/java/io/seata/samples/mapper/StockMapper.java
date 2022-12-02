package io.seata.samples.mapper;

import io.seata.samples.bean.Stock;
import io.seata.samples.utils.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface StockMapper extends MyMapper<Stock> {

    @Insert({"<script>",
            "INSERT sys_stock (quantity,price) VALUES (#{quantity},#{price}) on duplicate key update quantity=#{quantity}  ",
            "</script>"})
    Boolean addOrUpdateStock(@Param("quantity") BigDecimal quantity,@Param("price") BigDecimal price);


    @Insert({"<script>",
            "INSERT sys_stock VALUES (#{stockId},#{quantity},#{price}) on duplicate key update quantity=#{quantity}",
            "</script>"})
    Boolean addOrUpdateStock2(Long stockId, BigDecimal quantity, BigDecimal price);
}
