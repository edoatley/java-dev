package uk.org.edoatley.server;

import uk.org.edoatley.config.ConfigurationManager;

public class JettyFactory {
    private static final String SERVER_KEYSTORE = "server.keystore";
    private static final String SERVER_KEYSTORE_PASSWORD = "server.keystore.password";
    private static final String SERVER_PORT = "server.port";

    public static Jetty newSecureInstance() {
        int port = Integer.parseInt(ConfigurationManager.get(SERVER_PORT));
        String keystore = ConfigurationManager.get(SERVER_KEYSTORE);
        String keystorePassword = ConfigurationManager.get(SERVER_KEYSTORE_PASSWORD);
        return new Jetty(port, keystore, keystorePassword);
    }

    public static Jetty newInsecureInstance() {
        int port = Integer.parseInt(ConfigurationManager.get(SERVER_PORT));
        return new Jetty(port);
    }
}
