package uk.org.edoatley;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.edoatley.config.ConfigurationManager;
import uk.org.edoatley.server.Jetty;
import uk.org.edoatley.server.JettyFactory;

public class App {

    private static final String CONFIG_LOCATION_ENV_VAR = "CONFIG_LOCATION";
    private static final String DEFAULT_CONFIG_PATH = "config.properties";
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        initializeConfigurationProperties();

        try (Jetty webapp = JettyFactory.newSecureInstance();) {
            webapp.startServer(true);
            log.info("Application Server started");
        }
    }

    private static void initializeConfigurationProperties() {
        String configLocation = DEFAULT_CONFIG_PATH;
        if (System.getenv(CONFIG_LOCATION_ENV_VAR) != null) {
            configLocation = System.getenv(CONFIG_LOCATION_ENV_VAR);
        }
        ConfigurationManager.initialise(configLocation, true);
    }
}
