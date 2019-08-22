package io.seata.samples.api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The type Properties util.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public class PropertiesUtil {

    /**
     * Gets propertie value.
     *
     * @param path the path
     * @param key  the key
     * @return the propertie value
     */
    public static String getPropertieValue(String path, String key) {
        return getPropertieValue(path, key, null);

    }

    /**
     * Gets propertie value.
     *
     * @param path         the path
     * @param key          the key
     * @param defaultValue the default value
     * @return the propertie value
     */
    public static String getPropertieValue(String path, String key, String defaultValue) {
        Properties properties = new Properties();
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
        try {
            properties.load(in);
        } catch (IOException ignore) {
        }
        String value = properties.getProperty(key);
        return value == null ? defaultValue : value;
    }
}

