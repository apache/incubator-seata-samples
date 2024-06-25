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

import org.apache.seata.generator.DockerFileForJarGenerator;
import org.apache.seata.config.ConfigConstants;
import org.apache.seata.model.E2EConfig;
import org.apache.seata.model.Module;
import org.apache.seata.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class ImageBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageBuilder.class);

    public void BuildImage(E2EConfig e2EConfig, File moduleDir) throws IOException, InterruptedException {
        int exitCode = packageMavenParentModule(moduleDir);
        if (exitCode == 0) {
            buildDockerImage(moduleDir, e2EConfig);
        }
    }

    private int packageMavenParentModule(File moduleDir) throws IOException, InterruptedException {
        LOGGER.info("Packaging Maven module: " + moduleDir.getPath());
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(moduleDir);
//        builder.command("mvn.cmd", "clean", "package");
        if (System.getProperty("os.name").contains("Windows")) {
            builder.command("mvn.cmd", "clean", "package");
        } else {
            builder.command("mvn", "clean", "package");
        }
        Process process = builder.start();
        Utils.printProcessLog(LOGGER, process);
        int exitCode = process.waitFor();
        LOGGER.info(String.format("Maven module %s packaging finished with exit code %d", moduleDir, exitCode));
        return exitCode;
    }

    private void buildDockerImage(File parentModuleDir, E2EConfig e2EConfig) throws IOException, InterruptedException {
        LOGGER.info("Building Docker image for maven parent module: " + parentModuleDir.getPath());
        File[] files = parentModuleDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (copyJarInMavenChildModule(file, e2EConfig) == null) {
                        LOGGER.warn("Copying jar in Maven child module failed: " + file.getPath());
                    }
                }
            }
        }
        prepareDockerfile(e2EConfig);
        runDockerBuild(e2EConfig);
    }

    private void runDockerBuild(E2EConfig e2EConfig) throws IOException, InterruptedException {
        LOGGER.info(String.format("Building Docker image for scene %s", e2EConfig.getScene_name()));
        String tmpDir = ConfigConstants.IMAGE_DIR;
        File composeDir = new File(tmpDir);
        List<Module> modules = new ArrayList<>();
        modules.addAll(e2EConfig.getModules().getConsumers());
        modules.addAll(e2EConfig.getModules().getProviders());
        for (Module module : modules) {
            String moduleComposeDir = new File(composeDir, e2EConfig.getScene_name() + "-"
                    + module.getName()).getAbsolutePath();
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(moduleComposeDir));
            builder.command("docker", "build", "-t", String.format("%s:%s", module.getName(), "0.0.1"), ".");
            Process process = builder.start();
            Utils.printProcessLog(LOGGER, process);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                LOGGER.warn(String.format("Docker image for module %s build failed with exit code %d", module.getName(), exitCode));
            }
        }
    }

    private void prepareDockerfile(E2EConfig e2EConfig) {
        LOGGER.info(String.format("Generating Dockerfile for scene %s", e2EConfig.getScene_name()));
        DockerFileForJarGenerator generator = new DockerFileForJarGenerator();
        generator.generateDockerFiles(e2EConfig);
    }

    private File copyJarInMavenChildModule(File moduleDir, E2EConfig e2EConfig) throws IOException {
        LOGGER.info("Copying jar in Maven child module: " + moduleDir.getPath());
        File targetDir = new File(moduleDir, "target");
        if (targetDir.exists() && targetDir.isDirectory()) {
            File[] files = targetDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (files != null && files.length == 1) {
                Path dir = Paths.get(ConfigConstants.IMAGE_DIR, e2EConfig.getScene_name() + "-"
                        + moduleDir.getName());
                Files.createDirectories(dir);
                Path destPath = Paths.get(dir.toAbsolutePath().toString(), moduleDir.getName() + ".jar");
                Files.copy(files[0].toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                LOGGER.info("Copying jar in Maven child module success" + moduleDir.getPath());
                return destPath.toFile();
            }
            return null;
        }
        return null;
    }
}
