package io.seata.samples.mapper;

import io.seata.samples.bean.Order;
import io.seata.samples.utils.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface OrderMapper extends MyMapper<Order> {
    @Update({"<script>",
        "UPDATE sys_order so JOIN sys_stock ss ON ss.id=so.stock_id SET so.quantity=#{quantity} WHERE so.id=#{orderId} AND so.account_id=#{accountId} AND so.stock_id=#{stockId}",
        "</script>"})
    int updateOrder(@Param("accountId") Long accountId, @Param("orderId") Long orderId, @Param("stockId") Long stockId, @Param("quantity") Long quantity);



    @Insert({"<script>",
            "INSERT sys_order (id,order_number,account_id,stock_id,quantity,note) " +
                    "VALUES (#{id},#{orderNumber},#{accountId},#{stockId},#{quantity},#{note}) " +
                    "on duplicate key update quantity=#{quantity}  ",
            "</script>"})
    Integer  createOrUpdateOrder(@Param("id") Long id,@Param("orderNumber") Long orderNumber,@Param("accountId")  Long accountId, @Param("stockId") Long stockId, @Param("quantity") Long quantity,@Param("note") String note);


    @Insert({"<script>",
            "INSERT sys_order " +
                    "VALUES (#{id},#{orderNumber},#{accountId},#{stockId},#{quantity},#{amount},#{note}) " +
                    "on duplicate key update quantity=#{quantity}  ",
            "</script>"})
    Integer createOrUpdateOrder2(@Param("id") Long id, @Param("orderNumber") Long orderNumber, @Param("accountId")  Long accountId, @Param("stockId") Long stockId, @Param("quantity") Long quantity, @Param("amount")BigDecimal amount, @Param("note") String note);



}
