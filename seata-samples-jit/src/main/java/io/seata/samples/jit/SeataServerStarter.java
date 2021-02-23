package io.seata.samples.jit;

import io.seata.server.Server;

/**
 * @author ppf
 */
public class SeataServerStarter extends AbstractStarter {

    @Override
    protected void start0(String[] args) throws Exception {
        Server server = new Server();
        server.main(args);
        System.out.println("started seata");
        new ApplicationKeeper().keep();
    }
}
