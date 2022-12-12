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

import io.seata.samples.bean.Stock;
import io.seata.samples.utils.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface StockMapper extends MyMapper<Stock> {

    @Insert({"<script>",
            "INSERT sys_stock (quantity,price) VALUES (#{quantity},#{price}) on duplicate key update quantity=#{quantity}  ",
            "</script>"})
    Boolean addOrUpdateStock(@Param("quantity") BigDecimal quantity, @Param("price") BigDecimal price);


    @Insert({"<script>",
            "INSERT sys_stock VALUES (#{stockId},#{quantity},#{price}) on duplicate key update quantity=#{quantity}",
            "</script>"})
    Integer addOrUpdateStock2(@Param("stockId") Long stockId, @Param("quantity") BigDecimal quantity, @Param("price") BigDecimal price);
}
