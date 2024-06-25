/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.seata.e2e;

import org.apache.seata.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
@RestController
public class E2EController {
    @Autowired
    private BusinessService businessService;

    @GetMapping("testRollback")
    public void testRollback(HttpServletResponse response) throws IOException {
        Map<String, String> res = new HashMap<>();
        // 设置响应类型
        response.setContentType("text/yaml");
        response.setCharacterEncoding("UTF-8");

        Yaml yaml = new Yaml();
        try {
            businessService.purchaseRollback("U100001", "C00321", 2);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("res", "rollback");
            String yamlStr = yaml.dump(res);
            response.getWriter().write(yamlStr);
            return;
        }
        res.put("res", "commit");
        String yamlStr = yaml.dump(res);
        response.getWriter().write(yamlStr);
    }

    @GetMapping("testCommit")
    public void testCommit(HttpServletResponse response) throws IOException {
        Map<String, String> res = new HashMap<>();
        // 设置响应类型
        response.setContentType("text/yaml");
        response.setCharacterEncoding("UTF-8");

        Yaml yaml = new Yaml();
        businessService.purchaseCommit("U100001", "C00321", 2);
        res.put("res", "commit");
        String yamlStr = yaml.dump(res);
        response.getWriter().write(yamlStr);
    }
}
