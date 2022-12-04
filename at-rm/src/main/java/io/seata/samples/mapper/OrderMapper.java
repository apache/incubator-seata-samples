/*
 *  Copyright 1999-2022 Seata.io Group.
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
package io.seata.samples.mapper;

import io.seata.samples.bean.Order;
import io.seata.samples.utils.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

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


    Integer createOrUpdateBatchOrder(@Param("orders") List<Order> orders);
}
