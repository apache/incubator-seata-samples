/*
 *  Copyright 1999-2021 Seata.io Group.
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
package io.seata.samples.integration.stock.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.seata.samples.integration.stock.entity.TStock;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface TStockMapper extends BaseMapper<TStock> {

    /**
     * 扣减商品库存
     *
     * @Param: commodityCode 商品code  count扣减数量
     * @Return:
     */
    int decreaseStock(@Param("commodityCode") String commodityCode, @Param("count") Integer count);
}
