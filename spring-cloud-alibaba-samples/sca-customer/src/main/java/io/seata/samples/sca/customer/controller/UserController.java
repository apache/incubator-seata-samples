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
package io.seata.samples.sca.customer.controller;

import io.seata.core.context.RootContext;
import io.seata.samples.sca.common.domain.TbUser;
import io.seata.samples.sca.common.dubbo.api.UserService;
import io.seata.samples.sca.customer.mapper.TbUserMapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:数据库新建表
 *
 * CREATE TABLE `tb_user` (
 * `id` int(11) NOT NULL AUTO_INCREMENT,
 * `name` varchar(25) NOT NULL,
 * `age` int(3) DEFAULT NULL,
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
 *
 * author: yu.hb
 * Date: 2019-11-01
 */
@RestController
@Slf4j
public class UserController {

    @Reference
    private UserService userService;

    @Autowired
    private TbUserMapper userMapper;

    /**
     * seata 全局事务控制
     *
     * @param user
     */
    @PostMapping("/seata/user/add")
    @GlobalTransactional(rollbackFor = Exception.class) // 开启全局事务
    public void add(@RequestBody TbUser user) {
        log.info("globalTransactional begin, Xid:{}", RootContext.getXID());
        // local save
        localSave(user);

        // call provider save
        userService.add(user);

        // test seata globalTransactional
        throw new RuntimeException();
    }

    private void localSave(TbUser user) {
        user.setName("customer");
        userMapper.insertSelective(user);
    }
}
