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

    static Server server = null;

    public static void main(String[] args) throws IOException {
        server = new Server();
        server.main(args);
    }

}
