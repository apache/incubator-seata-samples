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
package io.seata.samples.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.seata.core.exception.TransactionException;
import io.seata.samples.entity.Product;
import io.seata.samples.service.DemoService;
import io.seata.samples.service.IProductService;

/**
 * <p>
 * 文件表 前端控制器
 * </p>
 *
 * @author funkye
 * @since 2019-03-20
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);
    @Reference(version = "1.0.0", timeout = 60000)
    DemoService demoService;
    @Reference(version = "1.0.0", timeout = 60000)
    private IProductService productService;

    /**
     * 测试mp分页插件是否生效
     *
     * @return
     */
    @GetMapping(value = "pageByProduct")
    public Object pageByProduct() {
        return productService.page(new Page<Product>(1, 10));
    }

    /**
     * 测试事务提交
     *
     * @return
     * @throws TransactionException
     */
    @GetMapping(value = "testCommit")
    public Object testCommit() throws TransactionException {
        return demoService.testCommit();
    }

    /**
     * 测试事务回滚
     *
     * @return
     * @throws TransactionException
     */
    @GetMapping(value = "testRollback")
    public Object testRollback() throws TransactionException {
        return demoService.testRollback();
    }

}
