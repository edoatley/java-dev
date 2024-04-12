package uk.org.edoatley;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.edoatley.server.Jetty;
import uk.org.edoatley.utils.PropertiesReader;

public class App {
    public static final String DEFAULT_CONFIG_PATH = "config.properties";
    private static final String SERVER_KEYSTORE = "server.keystore";
    private static final String SERVER_KEYSTORE_PASSWORD = "server.keystore.password";
    public static final String SERVER_PORT = "server.port";
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        Properties props = PropertiesReader.readProperties(DEFAULT_CONFIG_PATH);
        int port = Integer.parseInt(props.getProperty(SERVER_PORT));
        String keystore = props.getProperty(SERVER_KEYSTORE);
        String keystorePassword = props.getProperty(SERVER_KEYSTORE_PASSWORD);

        try (Jetty webapp = new Jetty(port, keystore, keystorePassword)) {
            webapp.startServer(true);
            log.info("Application Server started");
        }
    }

}
