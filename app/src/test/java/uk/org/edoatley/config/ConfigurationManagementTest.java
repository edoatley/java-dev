package uk.org.edoatley.config;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

public class ConfigurationManagementTest {
    private static String CONFIG_FILE_LOCATION = "src/test/resources/test-config.properties";

    @Test
    void testGetServerPort() {
        ConfigurationManagement cm = new ConfigurationManagement(CONFIG_FILE_LOCATION);
        assertEquals(9090, cm.getAsInt(ConfigurationManagement.SERVER_PORT).intValue());
    }

    @Test
    void testGetContextPath() {
        ConfigurationManagement cm = new ConfigurationManagement(CONFIG_FILE_LOCATION);
        assertEquals("/test/", cm.get(ConfigurationManagement.SERVER_CONTEXT_PATH));
    }
}
