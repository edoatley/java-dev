package uk.org.edoatley.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ITestConfiguration {
    private static final String ITEST_CONFIG_LOCATION_ENV_VAR = "ITEST_CONFIG_LOCATION";
    private static final Logger log = LoggerFactory.getLogger(ITestConfiguration.class);
    private static final String DEFAULT_CONFIG_FILE_LOCATION = "itest-config.properties";

    private ConfigurationManager config;

    public ITestConfiguration() {
        String configLocation = DEFAULT_CONFIG_FILE_LOCATION;
        if (System.getenv(ITEST_CONFIG_LOCATION_ENV_VAR) != null) {
            configLocation = System.getenv(ITEST_CONFIG_LOCATION_ENV_VAR);
        }
        config = new ConfigurationManager(configLocation);
    }

    public String get(String key) {
        return config.get(key);
    }
}
