package org.apache.seata.util;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class Utils {
    public static void printProcessLog(Logger LOGGER, Process process) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // 分别为标准输出和错误输出创建并启动线程
        executor.submit(() -> printStream(LOGGER, process.getInputStream(), false));
        executor.submit(() -> printStream(LOGGER, process.getErrorStream(), true));

        executor.shutdown();
    }

    private static void printStream(Logger LOGGER, InputStream inputStream, boolean isError) {
        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(line -> {
            if (isError) {
                LOGGER.warn(line);
            } else {
                LOGGER.info(line);
            }
        });
    }
}
