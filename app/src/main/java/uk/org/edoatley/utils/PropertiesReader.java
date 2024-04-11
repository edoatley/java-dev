package uk.org.edoatley.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesReader {
    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);

    private static final String MAIN_RESOURCES = "src/main/resources";
    private static final String TEST_RESOURCES = "src/test/resources";

    /**
     * Extract a properties file from the classpath or filesystem
     * 
     * @param filename can be an absolute path or a relative path to the classpath
     * @return Properties object populated with the properties from the file
     */
    public static Properties readProperties(String filename) {
        Properties properties = new Properties();

        // 1. assume properties file on the classpath and we are running as a jar
        try {
            log.info("Attempting to retrieve properties file from classpath, {}", filename);
            Optional<InputStream> onClasspathStream = lookOnClasspath(filename);
            if (onClasspathStream.isPresent()) {
                log.info(filename + " found on classpath");
                properties.load(onClasspathStream.get());
                return properties;
            }
        } catch (IOException e) {
            log.warn("Failed to read properties file from classpath", e);
        }

        // 2. assume we are running the code directly in an IDE and try
        // src/main/resource and src/test/resources
        try {
            log.info("Attempting to retrieve properties file from {} on classpath, {}", MAIN_RESOURCES, filename);
            Optional<InputStream> mainResourcesStream = lookOnClasspath(MAIN_RESOURCES + filename);
            if (mainResourcesStream.isPresent()) {
                log.info("{}  found in {} on classpath", filename, MAIN_RESOURCES);
                properties.load(mainResourcesStream.get());
                return properties;
            }
        } catch (IOException e) {
            log.warn("Failed to read properties file from classpath", e);
        }

        try {
            log.info("Attempting to retrieve properties file from {} on classpath, {}", TEST_RESOURCES, filename);
            Optional<InputStream> testResourcesStream = lookOnClasspath(TEST_RESOURCES + filename);
            if (testResourcesStream.isPresent()) {
                log.info("{}  found in {} on classpath", filename, TEST_RESOURCES);
                properties.load(testResourcesStream.get());
                return properties;
            }
        } catch (IOException e) {
            log.warn("Failed to read properties file from classpath", e);
        }

        // Third assume the properties file is on the filesystem and we are supplied an
        // absolute path
        try {
            properties.load(new FileInputStream(filename));
            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read properties file " + filename, e);
        }
    }

    private static Optional<InputStream> lookOnClasspath(String filename) {
        try (InputStream stream = PropertiesReader.class.getClassLoader().getResourceAsStream(filename)) {
            return Optional.of(stream);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
