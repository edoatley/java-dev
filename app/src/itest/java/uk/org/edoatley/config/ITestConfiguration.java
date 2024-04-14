package uk.org.edoatley.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.edoatley.utils.PropertiesReader;

public class ITestConfiguration {
    private static final String ITEST_CONFIG_LOCATION_ENV_VAR = "ITEST_CONFIG_LOCATION";
    private static final Logger log = LoggerFactory.getLogger(ITestConfiguration.class);
    private static final String DEFAULT_CONFIG_FILE_LOCATION = "itest-config.properties";

    private Set<String> keys;

    private Map<String, String> config;

    public ITestConfiguration() {
        keys = new HashSet<>();
        keys.add("server.port");
        keys.add("server.hostname");

        initialiseConfig();

        config.entrySet().forEach(
                entry -> log.debug("ITestConfiguration: {}={}", entry.getKey(), entry.getValue()));
    }

    /**
     * This method will read the service.url value from the configuration file
     * src/itest/resources/config.properties but will override it with the value from the
     * environment variable SERVICE_URL if it is set
     */
    private void initialiseConfig() {
        Optional<String> configLocation =
                Optional.ofNullable(System.getenv(ITEST_CONFIG_LOCATION_ENV_VAR));
        Properties properties = PropertiesReader
                .readProperties(configLocation.orElse(DEFAULT_CONFIG_FILE_LOCATION));

        config = new HashMap<>();
        keys.forEach(key -> {
            String configuredValue = properties.getProperty(key);
            String envKey = key.toUpperCase().replace(".", "_");
            String envValue = System.getenv(envKey);
            if (envValue == null && configuredValue == null) {
                log.error("Configuration key {} not found in properties file or environment", key);
            } else {
                config.put(key, envValue != null ? envValue : configuredValue);
            }
        });
    }

    public String get(String key) {
        return config.get(key);
    }
}
