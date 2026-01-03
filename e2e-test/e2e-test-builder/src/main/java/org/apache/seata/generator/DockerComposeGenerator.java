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
import org.apache.seata.model.E2EConfig;
import org.apache.seata.model.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.apache.seata.config.ConfigConstants.COMPOSE_FILE;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class DockerComposeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerComposeGenerator.class);
    private final Configuration cfg;

    public DockerComposeGenerator() {
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

    public void generateDockerComposeFile(E2EConfig e2EConfig, File file) {
        try {
            Modules modules = e2EConfig.getModules();
            Map<String, Object> map = new HashMap<>();
            map.put("modules", modules);
            
            File composeFile = new File(file, COMPOSE_FILE);
            cfg.getTemplate("dockercompose.ftl")
                    .process(map, new FileWriter(composeFile));
            
            // Log the generated docker-compose.yaml content
            LOGGER.info("==========================================");
            LOGGER.info("Generated docker-compose.yaml for scene: {}", e2EConfig.getScene_name());
            LOGGER.info("Location: {}", composeFile.getAbsolutePath());
            LOGGER.info("==========================================");
            
            try {
                String content = new String(Files.readAllBytes(composeFile.toPath()));
                LOGGER.info("Content:\n{}", content);
                LOGGER.info("==========================================");
            } catch (IOException ex) {
                LOGGER.warn("Could not read docker-compose.yaml content for logging", ex);
            }
            
        } catch (TemplateException | IOException e) {
            LOGGER.error(String.format("generate docker-compose file for %s fail", e2EConfig.getScene_name()
                    + "-" + e2EConfig.getScene_name()), e);
        }
    }
}
