package io.seata.samples.integration.common;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import io.seata.common.util.CollectionUtils;
import io.seata.common.util.NetUtil;
import io.seata.common.util.StringUtils;
import io.seata.config.Configuration;
import io.seata.config.ConfigurationFactory;
import io.seata.discovery.registry.RegistryService;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperRegisterServiceImpl implements RegistryService<IZkChildListener> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegisterServiceImpl.class);
    private static volatile ZookeeperRegisterServiceImpl instance;
    private static volatile ZkClient zkClient;
    private static final Configuration FILE_CONFIG;
    private static final String ZK_PATH_SPLIT_CHAR = "/";
    private static final String FILE_ROOT_REGISTRY = "registry";
    private static final String FILE_CONFIG_SPLIT_CHAR = ".";
    private static final String REGISTRY_CLUSTER = "cluster";
    private static final String REGISTRY_TYPE = "zk";
    private static final String SERVER_ADDR_KEY = "serverAddr";
    private static final String AUTH_USERNAME = "username";
    private static final String AUTH_PASSWORD = "password";
    private static final String SESSION_TIME_OUT_KEY = "session.timeout";
    private static final String CONNECT_TIME_OUT_KEY = "connect.timeout";
    private static final String FILE_CONFIG_KEY_PREFIX = "registry.zk.";
    private static final String ROOT_PATH = "/registry/zk/";
    private static final String ROOT_PATH_WITHOUT_SUFFIX = "/registry/zk";
    private static final ConcurrentMap<String, List<InetSocketAddress>> CLUSTER_ADDRESS_MAP;
    private static final ConcurrentMap<String, List<IZkChildListener>> LISTENER_SERVICE_MAP;
    private static final int REGISTERED_PATH_SET_SIZE = 1;
    private static final Set<String> REGISTERED_PATH_SET;

    private ZookeeperRegisterServiceImpl() {
    }

    static ZookeeperRegisterServiceImpl getInstance() {
        if (null == instance) {
            Class var0 = ZookeeperRegisterServiceImpl.class;
            synchronized (ZookeeperRegisterServiceImpl.class) {
                if (null == instance) {
                    instance = new ZookeeperRegisterServiceImpl();
                }
            }
        }

        return instance;
    }

    @Override
    public void register(InetSocketAddress address) throws Exception {
        NetUtil.validAddress(address);
        String path = this.getRegisterPathByPath(address);
        this.doRegister(path);
    }

    private boolean doRegister(String path) {
        if (this.checkExists(path)) {
            return false;
        } else {
            this.createParentIfNotPresent(path);
            this.getClientInstance().createEphemeral(path, true);
            REGISTERED_PATH_SET.add(path);
            return true;
        }
    }

    private void createParentIfNotPresent(String path) {
        int i = path.lastIndexOf(47);
        if (i > 0) {
            String parent = path.substring(0, i);
            if (!this.checkExists(parent)) {
                this.getClientInstance().createPersistent(parent);
            }
        }

    }

    private boolean checkExists(String path) {
        return this.getClientInstance().exists(path);
    }

    @Override
    public void unregister(InetSocketAddress address) throws Exception {
        NetUtil.validAddress(address);
        String path = this.getRegisterPathByPath(address);
        this.getClientInstance().delete(path);
        REGISTERED_PATH_SET.remove(path);
    }

    @Override
    public void subscribe(String cluster, IZkChildListener listener) throws Exception {
        if (null != cluster) {
            String path = "/registry/zk/" + cluster;
            if (!this.getClientInstance().exists(path)) {
                this.getClientInstance().createPersistent(path);
            }

            this.getClientInstance().subscribeChildChanges(path, listener);
            LISTENER_SERVICE_MAP.putIfAbsent(cluster, new CopyOnWriteArrayList());
            ((List)LISTENER_SERVICE_MAP.get(cluster)).add(listener);
        }
    }

    @Override
    public void unsubscribe(String cluster, IZkChildListener listener) throws Exception {
        if (null != cluster) {
            String path = "/registry/zk/" + cluster;
            if (this.getClientInstance().exists(path)) {
                this.getClientInstance().unsubscribeChildChanges(path, listener);
                List<IZkChildListener> subscribeList = (List)LISTENER_SERVICE_MAP.get(cluster);
                if (null != subscribeList) {
                    List<IZkChildListener> newSubscribeList = (List)subscribeList.stream().filter((eventListener) -> {
                        return !eventListener.equals(listener);
                    }).collect(Collectors.toList());
                    LISTENER_SERVICE_MAP.put(cluster, newSubscribeList);
                }
            }

        }
    }

    @Override
    public List<InetSocketAddress> lookup(String key) throws Exception {
        String clusterName = this.getServiceGroup(key);
        return null == clusterName ? null : this.doLookup(clusterName);
    }

    List<InetSocketAddress> doLookup(String clusterName) throws Exception {
        boolean exist = this.getClientInstance().exists("/registry/zk/" + clusterName);
        if (!exist) {
            return null;
        } else {
            if (!LISTENER_SERVICE_MAP.containsKey(clusterName)) {
                List<String> childClusterPath = this.getClientInstance().getChildren("/registry/zk/" + clusterName);
                this.refreshClusterAddressMap(clusterName, childClusterPath);
                this.subscribeCluster(clusterName);
            }

            return (List)CLUSTER_ADDRESS_MAP.get(clusterName);
        }
    }

    @Override
    public void close() throws Exception {
        this.getClientInstance().close();
    }

    private ZkClient getClientInstance() {
        if (zkClient == null) {
            Class var1 = ZookeeperRegisterServiceImpl.class;
            synchronized (ZookeeperRegisterServiceImpl.class) {
                if (null == zkClient) {
                    zkClient = this.buildZkClient(FILE_CONFIG.getConfig("registry.zk.serverAddr"),
                        FILE_CONFIG.getInt("registry.zk.session.timeout"),
                        FILE_CONFIG.getInt("registry.zk.connect.timeout"),
                        FILE_CONFIG.getConfig("registry.zk.username"), FILE_CONFIG.getConfig("registry.zk.password"));
                }
            }
        }

        return zkClient;
    }

    ZkClient buildZkClient(String address, int sessionTimeout, int connectTimeout, String... authInfo) {
        ZkClient zkClient = new ZkClient(address, sessionTimeout == 0 ? 6000 : 6000, connectTimeout == 0 ? 2000 : 2000);
        if (!zkClient.exists("/registry/zk")) {
            zkClient.createPersistent("/registry/zk", true);
        }

        if (null != authInfo && authInfo.length == 2 && !StringUtils.isBlank(authInfo[0]) && !StringUtils.isBlank(
            authInfo[1])) {
            StringBuilder auth = (new StringBuilder(authInfo[0])).append(":").append(authInfo[1]);
            zkClient.addAuthInfo("digest", auth.toString().getBytes());
        }

        zkClient.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
            }

            @Override
            public void handleNewSession() throws Exception {
                ZookeeperRegisterServiceImpl.this.recover();
            }

            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {
            }
        });
        return zkClient;
    }

    private void recover() throws Exception {
        if (!REGISTERED_PATH_SET.isEmpty()) {
            REGISTERED_PATH_SET.forEach(this::doRegister);
        }

        if (!LISTENER_SERVICE_MAP.isEmpty()) {
            Map<String, List<IZkChildListener>> listenerMap = new HashMap(LISTENER_SERVICE_MAP);
            Iterator var2 = listenerMap.entrySet().iterator();

            while (true) {
                Map.Entry listenerEntry;
                List iZkChildListeners;
                do {
                    if (!var2.hasNext()) {
                        return;
                    }

                    listenerEntry = (Map.Entry)var2.next();
                    iZkChildListeners = (List)listenerEntry.getValue();
                } while (CollectionUtils.isEmpty(iZkChildListeners));

                Iterator var5 = iZkChildListeners.iterator();

                while (var5.hasNext()) {
                    IZkChildListener listener = (IZkChildListener)var5.next();
                    this.subscribe((String)listenerEntry.getKey(), listener);
                }
            }
        }
    }

    private void subscribeCluster(String cluster) throws Exception {
        this.subscribe(cluster, (parentPath, currentChilds) -> {
            String clusterName = parentPath.replace("/registry/zk/", "");
            if (CollectionUtils.isEmpty(currentChilds) && CLUSTER_ADDRESS_MAP.get(clusterName) != null) {
                CLUSTER_ADDRESS_MAP.remove(clusterName);
            } else if (!CollectionUtils.isEmpty(currentChilds)) {
                this.refreshClusterAddressMap(clusterName, currentChilds);
            }

        });
    }

    private void refreshClusterAddressMap(String clusterName, List<String> instances) {
        List<InetSocketAddress> newAddressList = new ArrayList();
        if (instances == null) {
            CLUSTER_ADDRESS_MAP.put(clusterName, newAddressList);
        } else {
            Iterator var4 = instances.iterator();

            while (var4.hasNext()) {
                String path = (String)var4.next();

                try {
                    String[] ipAndPort = path.split(":");
                    newAddressList.add(new InetSocketAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1])));
                } catch (Exception var7) {
                    LOGGER.warn("The cluster instance info is error, instance info:{}", path);
                }
            }

            CLUSTER_ADDRESS_MAP.put(clusterName, newAddressList);
        }
    }

    private String getClusterName() {
        String clusterConfigName = String.join(".", "registry", "zk", "cluster");
        return FILE_CONFIG.getConfig(clusterConfigName);
    }

    private String getRegisterPathByPath(InetSocketAddress address) {
        return "/registry/zk/" + this.getClusterName() + "/" + NetUtil.toStringAddress(address);
    }

    static {
        FILE_CONFIG = ConfigurationFactory.CURRENT_FILE_INSTANCE;
        CLUSTER_ADDRESS_MAP = new ConcurrentHashMap();
        LISTENER_SERVICE_MAP = new ConcurrentHashMap();
        REGISTERED_PATH_SET = Collections.synchronizedSet(new HashSet(1));
    }
}
