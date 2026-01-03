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
//            builder.inheritIO();
//            builder.command("docker-compose", "up", "--timeout", "120");
            builder.command("e2e", "run");
            Process process = builder.start();

            // Auto-respond to prompts by writing to stdin
            try (var out = process.getOutputStream()) {
                out.write("y\n".getBytes());
                out.flush();
            } catch (Exception e) {
                LOGGER.warn("Failed to write to process stdin", e);
            }

            printProcessLog(LOGGER, process);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                LOGGER.warn(String.format(" Seate e2e test %s by SkyWalking-E2E fail with exit code %d",
                        file.getName(), exitCode));

                // Print diagnostic information on failure
                printDiagnosticInfo(file);

                return exitCode;
            }
        } catch (Exception e) {
            LOGGER.error("Running Seate e2e test by SkyWalking-E2E fail in: " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return 0;
    }
    
    private static void printDiagnosticInfo(File testDir) {
        LOGGER.error("==========================================");
        LOGGER.error("DIAGNOSTIC INFORMATION FOR FAILED TEST");
        LOGGER.error("Test Directory: {}", testDir.getAbsolutePath());
        LOGGER.error("==========================================");
        
        // Print docker-compose.yaml
        File composeFile = new File(testDir, "docker-compose.yaml");
        if (composeFile.exists()) {
            try {
                String content = new String(java.nio.file.Files.readAllBytes(composeFile.toPath()));
                LOGGER.error("docker-compose.yaml content:\n{}", content);
            } catch (Exception e) {
                LOGGER.error("Failed to read docker-compose.yaml", e);
            }
        } else {
            LOGGER.error("docker-compose.yaml NOT FOUND!");
        }
        
        LOGGER.error("------------------------------------------");
        
        // Print e2e.yaml
        File e2eFile = new File(testDir, "e2e.yaml");
        if (e2eFile.exists()) {
            try {
                String content = new String(java.nio.file.Files.readAllBytes(e2eFile.toPath()));
                LOGGER.error("e2e.yaml content:\n{}", content);
            } catch (Exception e) {
                LOGGER.error("Failed to read e2e.yaml", e);
            }
        }
        
        LOGGER.error("------------------------------------------");
        
        // Try to get docker-compose logs
        try {
            LOGGER.error("Attempting to get docker-compose logs...");
            ProcessBuilder logsBuilder = new ProcessBuilder("docker-compose", "logs", "--tail=50");
            logsBuilder.directory(testDir);
            Process logsProcess = logsBuilder.start();
            printProcessLog(LOGGER, logsProcess);
            logsProcess.waitFor();
        } catch (Exception e) {
            LOGGER.error("Failed to get docker-compose logs", e);
        }
        
        LOGGER.error("==========================================");
    }
}
