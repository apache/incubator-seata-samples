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

import org.apache.seata.config.ConfigConstants;
import org.apache.seata.generator.DockerFileForJarGenerator;
import org.apache.seata.model.E2EConfig;
import org.apache.seata.model.Module;
import org.apache.seata.model.Replace;
import org.apache.seata.util.LogUtils;
import org.apache.seata.util.MavenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class ImageBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageBuilder.class);

    public void buildImage(E2EConfig e2EConfig, File moduleDir) throws IOException, InterruptedException {
        replaceFilesInModule(moduleDir, e2EConfig);
        int exitCode = packageMavenParentModule(moduleDir);
        revertFilesInModule(moduleDir, e2EConfig);
        if (exitCode == 0) {
            buildDockerImage(moduleDir, e2EConfig);
        }
    }

    private void revertFilesInModule(File moduleDir, E2EConfig e2EConfig) throws IOException {
        Path dir = Paths.get(ConfigConstants.REPLACE_DIR);
        if (e2EConfig.getReplace() != null) {
            for (Replace replace : e2EConfig.getReplace()) {
                if (replace.isExist()) {
                    File backup = new File(dir.toFile(), replace.getBackUp());
                    File dest = new File(moduleDir, replace.getDest());
                    Files.copy(backup.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(backup.toPath());
                }
            }
        }
        if (dir.toFile().exists()) {
            Files.delete(dir);
        }
    }

    private void replaceFilesInModule(File moduleDir, E2EConfig e2EConfig) throws IOException {
        if (e2EConfig.getReplace() != null) {
            Path dir = Paths.get(ConfigConstants.REPLACE_DIR);
            Files.createDirectories(dir);
            AtomicInteger backUpFileSuffixNum = new AtomicInteger();
            for (Replace replace : e2EConfig.getReplace()) {
                File source = new File(moduleDir, replace.getSource());
                File dest = new File(moduleDir, replace.getDest());
                if (dest.exists()) {
                    File backup = new File(dir.toFile(), source.getName() + backUpFileSuffixNum.getAndIncrement());
                    replace.setExist(true);
                    replace.setBackUp(backup.getName());
                    Files.copy(dest.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
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
        LogUtils.printProcessLog(LOGGER, process);
        int exitCode = process.waitFor();
        LOGGER.info(String.format("Maven module %s packaging finished with exit code %d", moduleDir, exitCode));
        return exitCode;
    }

    private void buildDockerImage(File parentModuleDir, E2EConfig e2EConfig) throws IOException, InterruptedException {
        LOGGER.info("Building Docker image for maven parent module: " + parentModuleDir.getPath());
        File[] files = parentModuleDir.listFiles();
        File pomFile = MavenUtils.getPomFile(parentModuleDir);
        if (files != null) {
            if (MavenUtils.hasSubModule(pomFile)) {
                copyJarInAllSubMavenModules(e2EConfig, files);
            } else {
                copyJarInMavenModule(parentModuleDir, e2EConfig);
            }
        }
        prepareDockerfile(e2EConfig);
        runDockerBuild(e2EConfig);
    }

    private void copyJarInAllSubMavenModules(E2EConfig e2EConfig, File[] files) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) {
                if (copyJarInMavenModule(file, e2EConfig) == null) {
                    LOGGER.warn("Copying jar in Maven child module failed: " + file.getPath());
                }
            }
        }
    }

    private void runDockerBuild(E2EConfig e2EConfig) throws IOException, InterruptedException {
        LOGGER.info(String.format("Building Docker image for scene %s", e2EConfig.getScene_name()));
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
            String moduleComposeDir = new File(composeDir, e2EConfig.getScene_name() + "-"
                    + module.getName()).getAbsolutePath();
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(moduleComposeDir));
            builder.command("docker", "build", "-t", String.format("%s:%s", module.getName(), "0.0.1"), ".");
            Process process = builder.start();
            LogUtils.printProcessLog(LOGGER, process);
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

    private File copyJarInMavenModule(File moduleDir, E2EConfig e2EConfig) throws IOException {
        LOGGER.info("Copying jar in Maven child module: " + moduleDir.getPath());
        File targetDir = new File(moduleDir, "target");
        if (targetDir.exists() && targetDir.isDirectory()) {
            File[] files = targetDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (files != null && files.length > 0) {
                // if there are many jar files, the one with the largest size is selected, because it may have dependencies
                Optional<File> fileWithDepend = Arrays.stream(files)
                        .filter(File::isFile)
                        .max(Comparator.comparingLong(File::length));
                if (!fileWithDepend.isPresent()) {
                    return null;
                }
                Path dir = Paths.get(ConfigConstants.IMAGE_DIR, e2EConfig.getScene_name() + "-"
                        + moduleDir.getName());
                Files.createDirectories(dir);
                Path destPath = Paths.get(dir.toAbsolutePath().toString(), moduleDir.getName() + ".jar");
                Files.copy(fileWithDepend.get().toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                LOGGER.info("Copying jar in Maven child module success" + moduleDir.getPath());
                return destPath.toFile();
            }
            return null;
        }
        return null;
    }
}
