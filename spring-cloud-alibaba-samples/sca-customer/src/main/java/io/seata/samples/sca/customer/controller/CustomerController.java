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

import io.seata.samples.sca.common.dubbo.api.DubboEchoService;
import io.seata.samples.sca.customer.feign.ProviderFeignService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yu.hb on 2019-10-30
 */
@RestController
@RefreshScope // Nacos动态刷新配置
public class CustomerController {

    @DubboReference
    private DubboEchoService dubboEchoService;

    @Autowired
    private ProviderFeignService providerFeignService;

    @Value("${user.name:yuhb}")
    private String name;

    @Value("${user.age:18}")
    private Integer age;

    /**
     * Dubbo 方式调用
     *
     * @param name
     * @return
     */
    @GetMapping("/dubbo/echo/{name}")
    public String dubboEcho(@PathVariable("name") String name) {
        return dubboEchoService.echo(name);
    }

    /**
     * Open Feign 方式调用
     *
     * @param name
     * @return
     */
    @GetMapping("/feign/echo")
    public String feignEcho(String name) {
        return providerFeignService.feignEcho(name);
    }

    /**
     * Nacos 动态获取配置
     * Nacos 控制台新建配置
     * dataid : common.yaml 并添加属性 user.name user.age
     *
     * @return
     */
    @GetMapping("/dynamicConfig")
    public String dubboEcho() {
        return dubboEchoService.echo(String.format("my name is %s, age is %d", name, age));
    }
}
