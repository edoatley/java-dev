package uk.org.edoatley;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.edoatley.config.ConfigurationManagement;
import uk.org.edoatley.server.Jetty;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final String CONFIG_PROPERTIES_FILE = "/config.properties";

    public static void main(String[] args) throws Exception {

        ConfigurationManagement config = new ConfigurationManagement(CONFIG_PROPERTIES_FILE);

        try (Jetty webapp = new Jetty(config.getAsInt(ConfigurationManagement.SERVER_PORT),
                config.get(ConfigurationManagement.SERVER_CONTEXT_PATH))) {
            webapp.startServer();

        } catch (Exception e) {
            log.error("Failed running web server", e);
        }

    }

}
