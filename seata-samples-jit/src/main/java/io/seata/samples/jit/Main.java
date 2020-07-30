package io.seata.samples.jit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * @author ppf
 */
public class Main {

    private static List<AbstractStarter> dependServers = new ArrayList<>(4);

    private static final ThreadFactory JIT_TF = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("seata-jit-%d")
            .setUncaughtExceptionHandler((t, a) -> a.printStackTrace()).build();
    private static final ThreadPoolExecutor JIT_POOL = new ThreadPoolExecutor(8, 32,
            120L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), JIT_TF, new CallerRunsPolicy());


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
