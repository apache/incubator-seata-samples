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
package org.apache.seata.builder;

import org.apache.commons.io.file.PathUtils;
import org.apache.seata.config.ConfigConstants;
import org.apache.seata.generator.DockerComposeGenerator;
import org.apache.seata.generator.SkyWalkingE2EFileGenerator;
import org.apache.seata.model.E2EConfig;
import org.apache.seata.model.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.apache.seata.config.ConfigConstants.E2E_FILES;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class SceneBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneBuilder.class);

    public void buildScene(E2EConfig e2EConfig, File file) throws IOException {
        initE2EConfigForSceneBuild(e2EConfig);
        Path sceneDir = Paths.get(ConfigConstants.SCENE_DIR, e2EConfig.getScene_name());
        Files.createDirectories(sceneDir);
        copyE2EResource(file, sceneDir);
        generateTestFiles(e2EConfig, sceneDir);
    }

    private void initE2EConfigForSceneBuild(E2EConfig e2EConfig) {
        for (Module provider : e2EConfig.getModules().getProviders()) {
            provider.getDocker_service().setImage(
                    provider.getName() + ":" + ConfigConstants.IMAGE_VERSION);
        }
        for (Module consumer : e2EConfig.getModules().getConsumers()) {
            consumer.getDocker_service().setImage(
                    consumer.getName() + ":" + ConfigConstants.IMAGE_VERSION);
        }
   }

    private void generateTestFiles(E2EConfig e2EConfig, Path sceneDir) {
        DockerComposeGenerator dockerComposeGenerator = new DockerComposeGenerator();
        dockerComposeGenerator.generateDockerComposeFile(e2EConfig, sceneDir.toFile());
        SkyWalkingE2EFileGenerator skyWalkingE2EFileGenerator = new SkyWalkingE2EFileGenerator();
        skyWalkingE2EFileGenerator.generateSkyWalkingE2EFile(e2EConfig, sceneDir.toFile());
    }

    private void copyE2EResource(File file, Path sceneDir) throws IOException {
        File[] resources = file.listFiles((tempFile) -> E2E_FILES.equals(tempFile.getName()));
        if (resources == null || resources.length != 1) {
            LOGGER.error(String.format("find e2e-files in file: %s", file));
            throw new RuntimeException("e2e-files not found");
        }
        PathUtils.copyDirectory(resources[0].toPath(), Paths.get(sceneDir.toString(), E2E_FILES),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
