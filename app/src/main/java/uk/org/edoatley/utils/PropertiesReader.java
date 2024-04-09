package uk.org.edoatley.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesReader {

    // This method will take a path which can be an absolute or relative path or a
    // path on the classpath and return a Properties object with the contents of the
    // file
    public static Properties readProperties(String filename) {
        Properties properties = new Properties();
        try {
            // Try loading from classpath
            properties.load(PropertiesReader.class.getClassLoader().getResourceAsStream(filename));
        } catch (Exception classpathException) {
            try {
                // Try loading from absolute file path
                properties.load(new FileInputStream(filename));
            } catch (Exception filePathException) {
                // Handle exceptions cleanly
                throw new RuntimeException("Failed to read properties file " + filename, filePathException);
            }
        }
        return properties;
    }
}
