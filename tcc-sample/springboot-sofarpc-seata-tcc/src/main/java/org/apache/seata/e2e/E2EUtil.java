package org.apache.seata.e2e;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class E2EUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(E2EUtil.class);
    private E2EUtil() {
    }

    public static void writeE2EResFile(String outPutRes, String name) {
        try {
            Files.write(Paths.get(name), outPutRes.getBytes());
        } catch (IOException e) {
           LOGGER.error("write E2EResFile error", e);
        }
    }

    public static boolean isInE2ETest() {
        Map<String, String> envs = System.getenv();
        String env = envs.getOrDefault("E2E_ENV", "");
        return "open".equals(env);
    }
}
