package io.seata.samples.tcc;


import io.seata.core.store.StoreMode;
import io.seata.server.Server;

import java.io.IOException;

/**
 * Mock a seata server
 *
 * @author zhangsen
 */
public class SeataServerStarter {

    static Server server = null;

    public static void main(String[] args) throws IOException {
        server = new Server();
        server.main(new String[]{"8091", StoreMode.FILE.name(), "127.0.0.1"});
    }

}
