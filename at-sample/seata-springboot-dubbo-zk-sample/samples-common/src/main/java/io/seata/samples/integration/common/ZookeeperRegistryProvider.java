package io.seata.samples.integration.common;

import io.seata.common.loader.LoadLevel;
import io.seata.discovery.registry.RegistryProvider;
import io.seata.discovery.registry.RegistryService;

@LoadLevel(name = "ZK", order = 100)
public class ZookeeperRegistryProvider implements RegistryProvider {
    public ZookeeperRegistryProvider() {
    }

    @Override
    public RegistryService provide() {
        return ZookeeperRegisterServiceImpl.getInstance();
    }
}
