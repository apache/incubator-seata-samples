/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            skyWalkingController.setE2eDir(args[0]);
        }
        skyWalkingController.runE2ETests();
    }
}
