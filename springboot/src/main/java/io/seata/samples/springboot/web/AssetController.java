/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
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

package io.seata.samples.springboot.web;

import io.seata.samples.springboot.service.AssignService;
import io.seata.samples.springboot.sys.domain.AssetAssign;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The type Asset controller.
 */
@Controller
@RequestMapping
public class AssetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetController.class);

    final String ASSET_ID = "14070e0e3cfe403098fa9ca37e8e7e76";

    @Autowired
    private AssignService assignService;

    /**
     * The Port.
     */
    @Value("${server.port}")
    String port;

    /**
     * Home string.
     *
     * @return the string
     */
    @RequestMapping(value = "/asset/assign")
    @ResponseBody
    public String assetAssign() {
        LOGGER.info("welcome to deposit");

        String result;
        try {
            AssetAssign assetAssign = assignService.increaseAmount(
                ASSET_ID);
            result = assetAssign.toString();
        } catch (Exception e) {
            result = ExceptionUtils.getMessage(e);

        }
        return result;
    }

}
