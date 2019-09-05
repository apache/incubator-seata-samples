package io.seata.samples.tcc.server;

import java.io.IOException;

import io.seata.core.store.StoreMode;
import io.seata.server.Server;

/**
 * Mock a seata server
 *
 * @author zhangsen
 */
public class SeataServerStarter {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        new FescarServer().init(args);
    }

    /**
     * The type Fescar server.
     */
    public static class FescarServer {

        /**
         * The Server.
         */
        Server server = null;

        /**
         * Init.
         *
         * @throws IOException the io exception
         */
        public void init(String[] args) throws IOException {
            server = new Server();
            server.main(args);
        }
    }
}
