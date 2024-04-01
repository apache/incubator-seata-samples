package org.apache.seata.config;

import org.apache.seata.model.E2EConfig;
import org.apache.seata.model.E2EWrapper;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class ConfigReader {
    public static E2EConfig readConfig(File configFile) throws IOException {
        Yaml yaml = new Yaml(new Constructor(E2EWrapper.class));
        E2EWrapper e2EWrapper = new E2EWrapper();
        InputStream inputStream = Files.newInputStream(configFile.toPath());
        // 从YAML文件读取数据并转换为Java对象
        e2EWrapper = yaml.load(inputStream);
        return e2EWrapper.getE2e();
    }
}
