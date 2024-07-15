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
import org.apache.seata.model.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class DockerFileForJarGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerFileForJarGenerator.class);
    private final Configuration cfg;

    public DockerFileForJarGenerator() {
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

    public void generateDockerFiles(E2EConfig e2EConfig) {
        String tmpDir = ConfigConstants.IMAGE_DIR;
        File composeDir = new File(tmpDir);
        List<Module> modules = new ArrayList<>();
        if (e2EConfig.getModules().getConsumers() != null && e2EConfig.getModules().getConsumers().size() > 0) {
            modules.addAll(e2EConfig.getModules().getConsumers());
        }
        if (e2EConfig.getModules().getProviders() != null && e2EConfig.getModules().getProviders().size() > 0) {
            modules.addAll(e2EConfig.getModules().getProviders());
        }
        for (Module module : modules) {
            if (ConfigConstants.DOCKER_SERVICE_JAR_TYPE.equals(module.getModuleType())) {
                generateJarDcokerFiles(e2EConfig, composeDir, module);
                continue;
            }
            generateApplicationDcokerFiles(e2EConfig, composeDir, module);
        }
    }

    private void generateApplicationDcokerFiles(E2EConfig e2EConfig, File composeDir, Module module) {
        String moduleComposeDir = new File(composeDir, e2EConfig.getScene_name() + "-"
                + module.getName()).getAbsolutePath();
        try {
            Map<String, Object> props = new HashMap<>();
            props.put("sourceJar", module.getName() + ".jar");
            cfg.getTemplate("application-dockerFile.ftl")
                    .process(props, new FileWriter(new File(moduleComposeDir, "Dockerfile")));
        } catch (TemplateException | IOException e) {
            LOGGER.error(String.format("generate docker file %s fail", e2EConfig.getScene_name()
                    + "-" + module.getName()), e);
        }
    }

    private void generateJarDcokerFiles(E2EConfig e2EConfig, File composeDir, Module module) {
        String moduleComposeDir = new File(composeDir, e2EConfig.getScene_name() + "-"
                + module.getName()).getAbsolutePath();
        try {
            Map<String, Object> props = new HashMap<>();
            props.put("sourceJar", module.getName() + ".jar");
            cfg.getTemplate("jar-dockerFile.ftl")
                    .process(props, new FileWriter(new File(moduleComposeDir, "Dockerfile")));
            URL entryPoint = this.getClass().getClassLoader().getResource("sh/entrypoint.sh");
            Path destPath = Paths.get(moduleComposeDir, "entrypoint.sh");
            Files.copy(Paths.get(entryPoint.toURI()), destPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (TemplateException | IOException | URISyntaxException e) {
            LOGGER.error(String.format("generate docker file %s fail", e2EConfig.getScene_name()
                    + "-" + module.getName()), e);
        }
    }
}
