package io.seata.samples.tcc;


import io.seata.core.store.StoreMode;
import io.seata.server.Server;

import java.io.File;
import java.io.IOException;

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
        server.main(new String[]{"8091", StoreMode.FILE.name(), "127.0.0.1"});

    }



}
