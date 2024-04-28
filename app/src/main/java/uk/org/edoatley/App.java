package uk.org.edoatley;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.edoatley.config.ConfigurationManager;
import uk.org.edoatley.config.DependencyInjection;
import uk.org.edoatley.config.JettyResourceConfig;
import uk.org.edoatley.server.Jetty;

public class App {

    private static final String CONFIG_LOCATION_ENV_VAR = "CONFIG_LOCATION";
    private static final String DEFAULT_CONFIG_PATH = "config.properties";
    private static final String SERVER_KEYSTORE = "server.keystore";
    private static final String SERVER_KEYSTORE_PASSWORD = "server.keystore.password";
    private static final String SERVER_PORT = "server.port";
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        ConfigurationManager config = configurationProperties();

        int port = Integer.parseInt(config.get(SERVER_PORT));
        String keystore = config.get(SERVER_KEYSTORE);
        String keystorePassword = config.get(SERVER_KEYSTORE_PASSWORD);

        ResourceConfig resourceConfig = new JettyResourceConfig(new DependencyInjection(config));
        try (Jetty webapp = new Jetty(resourceConfig, port, keystore, keystorePassword)) {
            webapp.startServer(true);
            log.info("Application Server started");
        }
    }

    private static ConfigurationManager configurationProperties() {
        String configLocation = DEFAULT_CONFIG_PATH;
        if (System.getenv(CONFIG_LOCATION_ENV_VAR) != null) {
            configLocation = System.getenv(CONFIG_LOCATION_ENV_VAR);
        }
        ConfigurationManager config = new ConfigurationManager(configLocation);
        return config;
    }
}
