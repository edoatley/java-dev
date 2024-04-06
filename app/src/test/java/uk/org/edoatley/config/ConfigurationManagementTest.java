package uk.org.edoatley.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConfigurationManagementTest {
    private static String CONFIG_FILE_LOCATION = "src/test/resources/test-config.properties";
    private static File CONFIG_FILE = new File(CONFIG_FILE_LOCATION);
    private InputStream resourceAsStream;

    @Test
    @DisplayName("Check the config file is there before using in tests")
    void testValidConfigFile() {
        assertTrue(CONFIG_FILE.exists());
    }

    @Test
    void testReadConfigFromJUnit() throws IOException {
        Properties props = new Properties();
        InputStream resourceAsStream = ConfigurationManagementTest.class.getResourceAsStream(CONFIG_FILE_LOCATION);
        props.load(resourceAsStream);
        props.list(System.err);
    }

    @Test
    void testGetServerPort() {
        ConfigurationManagement cm = new ConfigurationManagement(CONFIG_FILE.getAbsolutePath());
        assertEquals(9090, cm.getAsInt(ConfigurationManagement.SERVER_PORT).intValue());
    }

    @Test
    void testGetContextPath() {
        ConfigurationManagement cm = new ConfigurationManagement(CONFIG_FILE_LOCATION);
        assertEquals("/test/", cm.get(ConfigurationManagement.SERVER_CONTEXT_PATH));
    }
}
