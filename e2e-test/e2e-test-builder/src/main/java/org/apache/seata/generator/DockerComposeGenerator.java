package org.apache.seata.generator;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.seata.config.ConfigConstants;
import org.apache.seata.model.E2EConfig;
import org.apache.seata.model.Module;
import org.apache.seata.model.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.seata.config.ConfigConstants.COMPOSE_FILE;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class DockerComposeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerComposeGenerator.class);
    private final Configuration cfg;

    public DockerComposeGenerator() {
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        try {
            cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/template");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setNumberFormat("computer");
        } catch (Exception e) {
            // never to do this
        }
    }

    public void generateDockerComposeFile(E2EConfig e2EConfig, File file) {
        try {
            Modules modules = e2EConfig.getModules();
            Map<String, Object> map = new HashMap<>();
            map.put("modules", modules);
            cfg.getTemplate("dockercompose.ftl")
                    .process(map, new FileWriter(new File(file, COMPOSE_FILE)));
        } catch (TemplateException | IOException e) {
            LOGGER.error(String.format("generate docker-compose file for %s fail", e2EConfig.getScene_name()
                    + "-" + e2EConfig.getScene_name()), e);
        }
    }
}
