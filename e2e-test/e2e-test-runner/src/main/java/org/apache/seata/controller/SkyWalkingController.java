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
package org.apache.seata.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.apache.seata.util.LogUtils.printProcessLog;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class SkyWalkingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkyWalkingController.class);
    private String e2eDir;

    public String getE2eDir() {
        return e2eDir;
    }

    public void setE2eDir(String e2eDir) {
        this.e2eDir = e2eDir;
    }

    public void runE2ETests() {
        File e2eDir = new File(this.e2eDir);
        for (File file : e2eDir.listFiles()) {
            if (file.isDirectory()) {
                LOGGER.info("Running Seate e2e test by SkyWalking-E2E: " + file.getName());
                if (0 != runTest(file)) {
                    for (int i = 0; i < 5; i++) {
                        int onceTestCode = runTest(file);
                        if (onceTestCode != 0) {
                            System.exit(onceTestCode);
                        }
                    }
                }
            }
        }
    }

    private static int runTest(File file) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(file);
            builder.command("e2e", "run");
            Process process = builder.start();
            printProcessLog(LOGGER, process);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                LOGGER.warn(String.format(" Seate e2e test %s by SkyWalking-E2E fail with exit code %d",
                        file.getName(), exitCode));
                return exitCode;
            }
        } catch (Exception e) {
            LOGGER.error("Running Seate e2e test by SkyWalking-E2E fail in: " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return 0;
    }
}
