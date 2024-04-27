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
import org.apache.seata.config.ConfigReader;
import org.apache.seata.model.E2EConfig;

import java.io.File;
import java.io.IOException;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class E2EBuilder {
    private String rootPath;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void buildSeataE2ETest() throws IOException, InterruptedException {
        File root = new File(rootPath);
        searchAndBuild(root);
    }

    private void searchAndBuild(File dir) throws IOException, InterruptedException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchAndBuild(file);
                    File configFile = new File(file, ConfigConstants.SEATA_E2E_FILE);
                    if (configFile.exists()) {
                        E2EConfig e2EConfig = ConfigReader.readConfig(configFile);
                        ImageBuilder imageBuilder = new ImageBuilder();
                        imageBuilder.BuildImage(e2EConfig, file);
                        SceneBuilder sceneBuilder = new SceneBuilder();
                        sceneBuilder.buildScene(e2EConfig, file);
                    }
                }
            }
        }
    }
}
