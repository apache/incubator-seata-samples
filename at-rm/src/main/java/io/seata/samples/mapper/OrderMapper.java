package io.seata.samples.mapper;

import io.seata.samples.bean.Order;
import io.seata.samples.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface OrderMapper extends MyMapper<Order> {
    @Update({"<script>",
        "UPDATE sys_order so JOIN sys_stock ss ON ss.id=so.stock_id SET so.quantity=#{quantity} WHERE so.id=#{orderId} AND so.account_id=#{accountId} AND so.stock_id=#{stockId}",
        "</script>"})
    int updateOrder(@Param("accountId") Long accountId, @Param("orderId") Long orderId, @Param("stockId") Long stockId, @Param("quantity") Long quantity);
}
