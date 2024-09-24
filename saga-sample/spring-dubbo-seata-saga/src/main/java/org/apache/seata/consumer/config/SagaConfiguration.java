package org.apache.seata.consumer.config;

import org.apache.seata.saga.engine.StateMachineConfig;
import org.apache.seata.saga.engine.StateMachineEngine;
import org.apache.seata.saga.engine.config.DbStateMachineConfig;
import org.apache.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import org.apache.seata.saga.rm.StateMachineEngineHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangte
 * Create At 2024/1/20
 */
@Configuration
public class SagaConfiguration {

    @Bean
    public StateMachineConfig stateMachineConfig(DataSource dataSource) {
        DbStateMachineConfig dbStateMachineConfig = new DbStateMachineConfig();
        dbStateMachineConfig.setApplicationId("seata-saga-consumer");
        dbStateMachineConfig.setDataSource(dataSource);
        dbStateMachineConfig.setEnableAsync(true);
        dbStateMachineConfig.setResources(new String[]{"classpath*:statelang/*.json"});
        dbStateMachineConfig.setTxServiceGroup("my_test_tx_group");
        dbStateMachineConfig.setThreadPoolExecutor(new ThreadPoolExecutor(1, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100)));
        return dbStateMachineConfig;
    }

    @Bean
    public StateMachineEngine stateMachineEngine(StateMachineConfig stateMachineConfig) {
        ProcessCtrlStateMachineEngine processCtrlStateMachineEngine = new ProcessCtrlStateMachineEngine();
        processCtrlStateMachineEngine.setStateMachineConfig(stateMachineConfig);
        return processCtrlStateMachineEngine;
    }

    @Bean
    public StateMachineEngineHolder stateMachineEngineHolder(StateMachineEngine stateMachineEngine) {
        StateMachineEngineHolder stateMachineEngineHolder = new StateMachineEngineHolder();
        stateMachineEngineHolder.setStateMachineEngine(stateMachineEngine);
        return stateMachineEngineHolder;
    }
}
