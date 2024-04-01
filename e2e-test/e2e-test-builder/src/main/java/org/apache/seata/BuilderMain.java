package org.apache.seata;

import org.apache.seata.builder.E2EBuilder;

import java.io.IOException;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class BuilderMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        E2EBuilder e2EBuilder = new E2EBuilder();
        e2EBuilder.setRootPath("./");
        if (args != null && args.length == 1) {
            e2EBuilder.setRootPath(args[1]);
        }
        e2EBuilder.buildSeataE2ETest();
    }
}
