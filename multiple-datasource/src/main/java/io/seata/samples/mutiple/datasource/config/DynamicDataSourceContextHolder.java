package io.seata.samples.mutiple.datasource.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HelloWoodes
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(DataSourceKey.ORDER::name);

    private static List<Object> dataSourceKeys = new ArrayList<>();

    public static void setDataSourceKey(DataSourceKey key) {
        CONTEXT_HOLDER.set(key.name());
    }

    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

    public static List<Object> getDataSourceKeys() {
        return dataSourceKeys;
    }
}