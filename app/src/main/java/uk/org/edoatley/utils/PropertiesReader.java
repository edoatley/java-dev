package uk.org.edoatley.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesReader {
    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);

    /**
     * Extract a properties file from the classpath or filesystem
     * 
     * @param filename can be an absolute path or a relative path to the classpath
     * @return Properties object populated with the properties from the file
     */
    public static Properties readProperties(String filename) {
        Properties properties = new Properties();

        try {
            log.info("Attempting to retrieve properties file from classpath, {}", filename);
            InputStream onClasspathStream = PropertiesReader.class.getClassLoader().getResourceAsStream(filename);
            properties.load(new BufferedInputStream(onClasspathStream));
        } catch (IOException e) {
            log.debug("Failed to read properties file from classpath, {}", filename);
            try {
                log.info("Attempting to retrieve properties file file path, {}", filename);
                properties.load(new FileInputStream(filename));
            } catch (IOException ie) {
                log.error("Also failed to read properties file {}", filename);
                throw new RuntimeException("Failed to read properties file " + filename, ie);
            }
        }

        return properties;
    }
}
