package io.seata.samples.tcc;

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
     * The Server.
     */
    static Server server = null;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        server = new Server();
        server.main(args);

    }

}
