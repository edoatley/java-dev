package uk.org.edoatley.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.edoatley.utils.PropertiesReader;

/**
 * The ConfigurationManager class is responsible for managing the application configuration. It
 * provides methods to initialize the configuration, retrieve configuration values, and override
 * configuration values for testing purposes.
 */
/**
 * The ConfigurationManager class is responsible for managing the configuration properties of the
 * application. It provides methods to initialize the configuration, retrieve configuration values,
 * and override configuration values for testing purposes.
 */
public class ConfigurationManager {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationManager.class);

    private static Map<String, String> config = new HashMap<>();



    /**
     * Initializes the configuration manager with the specified configuration location and
     * environment usage flag.
     *
     * @param configLocation the location of the configuration file
     * @param useEnvironment flag indicating whether to use environment variables for configuration
     */
    public synchronized static void initialise(String configLocation, boolean useEnvironment) {
        Properties properties = PropertiesReader.readProperties(configLocation);

        initialiseConfig(properties, useEnvironment);

        // print result
        config.entrySet().forEach(entry -> log.debug("ConfigurationManager: {}={}", entry.getKey(),
                entry.getValue()));
    }

    /**
     * Retrieves the configuration value for the specified key.
     *
     * @param key the configuration key
     * @return the configuration value
     */
    public synchronized static String get(String key) {
        return config.get(key);
    }

    // expect this to only really be for testing
    /**
     * Overrides the value associated with the specified key in the configuration. If the key
     * already exists, its value will be updated. If the key does not exist, a new key-value pair
     * will be added to the configuration.
     *
     * @param key the key to override
     * @param value the new value to associate with the key
     */
    public synchronized static void override(String key, String value) {
        config.put(key, value);
    }


    /**
     * Initializes the configuration properties based on the provided properties and environment
     * variables. If the 'useEnvironment' flag is set to true, the method checks for corresponding
     * environment variables and uses them instead of the property values.
     *
     * @param properties The properties containing the configuration values.
     * @param useEnvironment A flag indicating whether to use environment variables or not.
     */
    private static void initialiseConfig(Properties properties, boolean useEnvironment) {
        properties.stringPropertyNames().forEach(key -> {
            String configuredValue = properties.getProperty(key);
            if (useEnvironment) {
                String envValue = getEnvValue(key);
                if (envValue != null) {
                    log.info(key + " - using env value:" + envValue + " instead of property value:"
                            + configuredValue);
                    configuredValue = envValue;
                }
            }
            log.info(key + "=" + config.get(key));
            config.put(key, configuredValue);
        });
    }

    /**
     * Retrieves the value of the specified environment variable.
     *
     * @param key the name of the environment variable
     * @return the value of the environment variable, or null if it is not set
     */
    private static String getEnvValue(String key) {
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);
        return envValue;
    }
}
