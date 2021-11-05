/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.samples.jit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import static java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * @author ppf
 */
public class Main {

    private static List<AbstractStarter> dependServers = new ArrayList<>(4);

    private static final ThreadFactory JIT_TF = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("seata-jit-%d")
        .setUncaughtExceptionHandler((t, a) -> a.printStackTrace()).build();
    private static final ThreadPoolExecutor JIT_POOL = new ThreadPoolExecutor(8, 32, 120L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), JIT_TF, new CallerRunsPolicy());

    static {
        dependServers.add(new SeataServerStarter());
    }

    public static void main(String[] args) throws Exception {
        startDepends(args);
        new ApplicationKeeper().keep();
    }

    public static void startDepends(String[] args) throws Exception {
        // 启动相关服务
        for (AbstractStarter dependServer : dependServers) {
            JIT_POOL.execute(() -> dependServer.start(args));
        }
    }

}
