package com.alibaba.fescar.samples.tcc.server;


import com.alibaba.fescar.server.Server;

import java.io.File;
import java.io.IOException;

/**
 * Mock a fescar server
 *
 * @author zhangsen
 */
public class FescarServerStarter {

    public static void main(String[] args) throws IOException {
        new FescarServer().init();
    }

    public static class FescarServer {

        Server server = null;

        public void init() throws IOException {
            server = new Server();
            String dataPath = new StringBuilder()
                    .append(System.getProperty("user.home")).append(File.separator)
                    .append("fescar").append(File.separator)
                    .append("sofarpc").append(File.separator)
                    .append("data").toString();
            server.main(new String[]{"8091", dataPath, "127.0.0.1"});
        }
    }
}
