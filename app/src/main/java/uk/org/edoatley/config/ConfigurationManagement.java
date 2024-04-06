package uk.org.edoatley.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationManagement {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationManagement.class);

    // server
    public static final String SERVER_PORT = "server.port";
    public static final String SERVER_CONTEXT_PATH = "server.context.path";

    private String configPath;
    private Properties properties;

    public ConfigurationManagement(String configPropertiesFile) {
        this.configPath = configPropertiesFile;
        this.properties = new Properties();
        importConfig();
    }

    private void importConfig() {
        System.err.println("configPath is " + configPath);
        try {
            properties.load(ConfigurationManagement.class.getResourceAsStream(configPath));
        } catch (IOException e) {
            log.error("Failed to read in configuration from " + configPath, e);
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public Integer getAsInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}
