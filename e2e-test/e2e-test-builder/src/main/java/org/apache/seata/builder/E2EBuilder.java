package org.apache.seata.builder;

import org.apache.seata.config.ConfigConstants;
import org.apache.seata.config.ConfigReader;
import org.apache.seata.model.E2EConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class E2EBuilder {
    private String rootPath;
    private static final Logger LOGGER = LoggerFactory.getLogger(E2EBuilder.class);

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
