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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.seata.util.LogUtils.printProcessLog;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class SkyWalkingController {
    public static final int RETRY_MAX_TIMES = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(SkyWalkingController.class);
    private String e2eDir;
    private static final Map<Character, Integer> caseOrder = new HashMap<>();

    public SkyWalkingController() {
        caseOrder.put('a', 1);
        caseOrder.put('x', 2);
        caseOrder.put('t', 3);
        caseOrder.put('s', 4);
    }

    public String getE2eDir() {
        return e2eDir;
    }

    public void setE2eDir(String e2eDir) {
        this.e2eDir = e2eDir;
    }

    public void runE2ETests() {
        File e2eDir = new File(this.e2eDir);
        List<String> passedProjects = new ArrayList<>();
        File[] files = e2eDir.listFiles();
        // use this order to run saga test first, because saga test is easy to fail
        List<File> filterFiles = Arrays.stream(files).sorted((a, b) -> {
            int scoreA = caseOrder.getOrDefault(a.getName().charAt(0), 0);
            int scoreB = caseOrder.getOrDefault(b.getName().charAt(0), 0);
            if (scoreA == scoreB) {
                return b.getName().compareTo(a.getName());
            }
            return scoreB - scoreA;
        }).collect(Collectors.toList());
        for (File file : filterFiles) {
            if (file.isDirectory()) {
                LOGGER.info("Running Seate e2e test by SkyWalking-E2E: " + file.getName());
                int onceTestCode = -1;
                for (int i = 0; i < RETRY_MAX_TIMES; i++) {
                    onceTestCode = runTest(file);
                    if (onceTestCode == 0) {
                       break;
                    }
                }
                if (onceTestCode != 0) {
                    printPassedCases(passedProjects);
                    System.exit(onceTestCode);
                }
                passedProjects.add(file.getName());
            }
        }
        printPassedCases(passedProjects);
    }

    private void printPassedCases(List<String> passedProjects) {
        LOGGER.info("Passed e2e cases are: ");
        for (String passedProject : passedProjects) {
            LOGGER.info(passedProject);
        }
    }

    private static int runTest(File file) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(file);
            //  builder.inheritIO();
            //  builder.command("docker-compose", "up", "--timeout", "120");
            builder.command("echo y | e2e run");
            // 启动进程
            Process process = builder.start();
            // 打印进程日志
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
