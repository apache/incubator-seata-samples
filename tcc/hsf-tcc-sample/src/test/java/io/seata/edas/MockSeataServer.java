package io.seata.edas;
import io.seata.server.Server;

import java.io.IOException;

/**
 * @author zhangsen
 * @data 2020-02-12
 */
public class MockSeataServer {

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
