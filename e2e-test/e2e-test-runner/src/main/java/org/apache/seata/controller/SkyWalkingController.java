package org.apache.seata.controller;

import org.apache.seata.generator.DockerComposeGenerator;
import org.apache.seata.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
                try {
                    ProcessBuilder builder = new ProcessBuilder();
                    builder.directory(file);
                    builder.command("e2e", "run");
                    Process process = builder.start();
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        LOGGER.warn(String.format(" Seate e2e test %s by SkyWalking-E2E fail with exit code %d",
                                file.getName(), exitCode));
                    }
                    Utils.printProcessLog(LOGGER, process);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
