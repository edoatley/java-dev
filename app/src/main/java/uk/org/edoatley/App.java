package uk.org.edoatley;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.edoatley.server.Jetty;
import uk.org.edoatley.utils.PropertiesReader;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static final String DEFAULT_CONFIG_PATH = "config.properties";
    public static final String SERVER_PORT = "server.port";

    public static void main(String[] args) throws Exception {

        Properties props = PropertiesReader.readProperties(DEFAULT_CONFIG_PATH);
        int port = Integer.parseInt(props.getProperty(SERVER_PORT));

        try (Jetty webapp = new Jetty(port)) {
            webapp.startServer(true);
            log.info("Application Server started");
        }
    }

}
