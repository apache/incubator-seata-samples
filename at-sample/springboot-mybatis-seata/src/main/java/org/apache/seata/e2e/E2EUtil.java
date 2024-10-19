package org.apache.seata.e2e;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class E2EUtil {
    public static void writeE2EResFile(String outPutRes) {
        try {
            Files.write(Paths.get("result.yaml"), outPutRes.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInE2ETest() {
        Map<String, String> envs = System.getenv();
        String env = envs.getOrDefault("E2E_ENV", "");
        return "open".equals(env);
    }
}
