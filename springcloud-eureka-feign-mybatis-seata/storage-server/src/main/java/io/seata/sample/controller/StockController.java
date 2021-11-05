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
package io.seata.sample.controller;

import io.seata.sample.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author IT云清
 */
@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private StockService stockServiceImpl;

    /**
     * 扣减库存
     *
     * @param productId 产品id
     * @param count     数量
     * @return
     */
    @RequestMapping("decrease")
    public String decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count) {
        stockServiceImpl.decrease(productId, count);
        return "Decrease stock success";
    }
}
