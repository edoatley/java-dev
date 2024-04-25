package uk.org.edoatley.config;

public class ITestConfiguration {
    private static final String ITEST_CONFIG_LOCATION_ENV_VAR = "ITEST_CONFIG_LOCATION";
    private static final String DEFAULT_CONFIG_FILE_LOCATION = "itest-config.properties";

    private ConfigurationManager config;

    public ITestConfiguration() {
        String configLocation = System.getenv(ITEST_CONFIG_LOCATION_ENV_VAR);
        if (configLocation == null) {
            configLocation = DEFAULT_CONFIG_FILE_LOCATION;
        }
        config = new ConfigurationManager(configLocation);
    }

    public String get(String key) {
        return config.get(key);
    }
}
