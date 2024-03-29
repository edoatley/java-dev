package uk.org.edoatley;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.eclipse.jetty.http2.server.Server;

public class App {

    private static final String CONFIG_PROPERTIES_FILE = "/config.properties";
    private static final String SERVER_PORT = "server.port";

    public static void main(String[] args) {
        Properties props = readConfiguration();

        int port = Integer.parseInt(props.getProperty(SERVER_PORT));

        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler();
    }

    private static Properties readConfiguration() throws IOException {
        Properties properties = new Properties();
        properties.load(App.class.getResourceAsStream(CONFIG_PROPERTIES_FILE));
        return properties;
    }
}
