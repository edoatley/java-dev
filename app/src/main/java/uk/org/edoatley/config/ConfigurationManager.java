package uk.org.edoatley.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.edoatley.utils.PropertiesReader;

public class ConfigurationManager {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationManager.class);
    private static final String DEFAULT_CONFIG_LOCATION = "config.properties";
    private static final boolean DEBUG = true;
    private Set<String> keys =
            Set.of("server.port", "server.hostname", "server.keystore", "server.keystore.password");

    private Map<String, String> config;

    public ConfigurationManager() {
        this(DEFAULT_CONFIG_LOCATION);
    }

    public ConfigurationManager(String configFile) {

        Properties properties = PropertiesReader.readProperties(configFile);

        initialiseConfig(properties);

        config.entrySet().forEach(entry -> log.debug("ConfigurationManager: {}={}", entry.getKey(),
                entry.getValue()));

        if (DEBUG) {
            log.info("ConfigurationManager: Configuration loaded from {}", configFile);
            keys.forEach(key -> log.info("Key {} has value {}", key, config.get(key)));
        }
    }

    private void initialiseConfig(Properties properties) {

        config = new HashMap<>();
        keys.forEach(key -> {
            String configuredValue = properties.getProperty(key);
            String envValue = getEnvValue(key);
            if (envValue == null && configuredValue == null) {
                log.error("Configuration key {} not found in properties file or environment", key);
            } else {
                config.put(key, envValue != null ? envValue : configuredValue);
            }
        });
    }

    private String getEnvValue(String key) {
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);
        return envValue;
    }

    public String get(String key) {
        return config.get(key);
    }
}
