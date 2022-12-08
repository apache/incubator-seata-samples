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
package io.seata.samples.service;

import java.math.BigDecimal;
import java.util.List;

import io.seata.samples.bean.Order;
import io.seata.spring.annotation.GlobalTransactional;

public interface BuyService {

    Boolean updateOrder(Long accountId, Long stockId, Long quantity, Long orderId, boolean success);
    Integer createOrUpdateOrder(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success);
    Integer createOrUpdateOrder2(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success);
    String addOrUpdateStockFail(BigDecimal quantity, BigDecimal price, boolean success);
    Integer addOrUpdateStock(Long stockId, BigDecimal quantity, BigDecimal price, boolean success);
    Integer createOrUpdateBatchOrderSuccess(List<Order> orders,boolean success);
    Boolean increaseAccountMoney(Long accountId, BigDecimal money);
}
