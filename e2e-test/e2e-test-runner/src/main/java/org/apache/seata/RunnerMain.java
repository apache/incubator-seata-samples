package org.apache.seata;

import org.apache.seata.config.ConfigConstants;
import org.apache.seata.controller.SkyWalkingController;

import java.io.IOException;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class RunnerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        SkyWalkingController skyWalkingController = new SkyWalkingController();
        skyWalkingController.setE2eDir(ConfigConstants.SCENE_DIR);
        if (args != null && args.length == 1) {
            skyWalkingController.setE2eDir(args[1]);
        }
        skyWalkingController.runE2ETests();
    }
}
