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
package org.apache.seata.generator;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.seata.config.ConfigConstants;
import org.apache.seata.model.E2EConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class SkyWalkingE2EFileGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkyWalkingE2EFileGenerator.class);
    private final Configuration cfg;

    public SkyWalkingE2EFileGenerator() {
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        try {
            cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/template");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setNumberFormat("computer");
        } catch (Exception e) {
            // never to do this
        }
    }

    public void generateSkyWalkingE2EFile(E2EConfig e2EConfig, File file) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("retry", e2EConfig.getRetry());
            map.put("cases", e2EConfig.getCases());
            cfg.getTemplate("skywalking-e2e.ftl")
                    .process(map, new FileWriter(new File(file, ConfigConstants.SKY_WALKING_E2E_FILE)));
        } catch (TemplateException | IOException e) {
            LOGGER.error(String.format("generate SkyWalking e2e test file for %s fail", e2EConfig.getScene_name()
                    + "-" + e2EConfig.getScene_name()), e);
        }
    }
}
