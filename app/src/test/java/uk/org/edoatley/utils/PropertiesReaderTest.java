package uk.org.edoatley.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReaderTest {

    @Test
    @DisplayName("Test reading properties from classpath")
    public void testReadPropertiesFromClasspath() {
        // Arrange
        Properties expectedProperties = new Properties();
        expectedProperties.setProperty("server.port", "9090");
        expectedProperties.setProperty("server.keystore", "tls/test-keystore.jks");
        expectedProperties.setProperty("server.keystore.password", "testjks");
        expectedProperties.setProperty("server.hostname", "restapi.edoatley.com");

        // Act
        Properties actualProperties = PropertiesReader.readProperties("test-config.properties");

        // Assert
        for (String key : expectedProperties.stringPropertyNames()) {
            Assertions.assertEquals(expectedProperties.getProperty(key),
                    actualProperties.getProperty(key));
        }
    }

    @Test
    @DisplayName("Test reading properties from absolute file path")
    public void testReadPropertiesFromAbsolutePath(@TempDir Path tempDir) {
        // Arrange
        String filename = tempDir.toString() + "/test.properties";
        Properties expectedProperties = new Properties();
        expectedProperties.setProperty("key1", "value1");
        expectedProperties.setProperty("key2", "value2");

        // Create a temporary properties file in the absolute file path
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            expectedProperties.store(fos, null);
        } catch (IOException e) {
            Assertions.fail("Failed to create temporary properties file");
        }

        // Act
        Properties actualProperties = PropertiesReader.readProperties(filename);

        // Assert
        Assertions.assertEquals(expectedProperties, actualProperties);

        // Clean up the temporary properties file
        File file = new File(filename);
        file.delete();
    }

    @Test
    @DisplayName("Test reading properties with invalid file path")
    public void testReadPropertiesWithInvalidFilePath() {
        // Arrange
        String filename = "nonexistent.properties";

        // Act and Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            PropertiesReader.readProperties(filename);
        });
    }
}
