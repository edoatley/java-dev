package uk.org.edoatley.server;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.edoatley.config.JettyResourceConfig;

public class Jetty implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(Jetty.class);
    private int configuredPort;

    private Server server;

    public Jetty(int configuredPort) {
        this.configuredPort = configuredPort;
        this.server = newServer(this.configuredPort);
    }

    public Jetty(int configuredPort, String keystore, String keystorePassword) {
        this.configuredPort = configuredPort;
        this.server = newSecureServer(this.configuredPort, keystore, keystorePassword);
    }

    public void startServer(boolean blocking) throws Exception {
        log.debug("Attempting to start server");
        server.start();
        log.info("Server started at {}", serviceUrl());

        if (blocking) {
            log.info("Server.join() blocking server for {} until further notice", serviceUrl());
            server.join();
        }
    }

    private static Server newServerNoConnector() {
        Server server = new Server();

        // Add root servlet
        ServletContextHandler contextHandler =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);
        log.debug("ServletContextHandler created");

        // Adds Jersey servlet that will handle requests on /api/*
        contextHandler.addServlet(new ServletContainer(new JettyResourceConfig()), "/api/*");
        log.debug("ServletHolder created");

        return server;
    }

    private Server newServer(int httpPort) {
        Server server = newServerNoConnector();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(httpPort);
        server.addConnector(connector);
        return server;
    }

    /**
     * Make the server provided secure
     * 
     * (based on code taken from
     * https://github.com/jetty/jetty-examples/blob/a93cfaf0b5f6a9d54c0b174739cddd5b9bda468e/embedded/ee10-websocket-jetty-api/src/main/java/examples/time/WebSocketTimeServer.java)
     * 
     * @param unsecuredServer a jetty server without TLS configuration
     * @return a jetty server with TLS configuration
     */
    private Server newSecureServer(int httpsPort, String keystore, String keystorePassword) {

        Server server = newServerNoConnector();

        ResourceFactory resourceFactory = ResourceFactory.of(server);
        PathResourceFactory pathResourceFactory = new PathResourceFactory();

        // Setup SSL
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory
                .setKeyStoreResource(findKeyStore(resourceFactory, pathResourceFactory, keystore));
        sslContextFactory.setKeyStorePassword(keystorePassword);
        sslContextFactory.setKeyManagerPassword(keystorePassword);
        sslContextFactory.addExcludeProtocols("TLSv1", "TLSv1.1");

        // Setup HTTPS Configuration
        HttpConfiguration httpsConf = new HttpConfiguration();
        httpsConf.setSecurePort(httpsPort);
        httpsConf.setSecureScheme("https");
        httpsConf.addCustomizer(new SecureRequestCustomizer()); // adds ssl info to request object

        // Establish the Secure ServerConnector
        ServerConnector httpsConnector =
                new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"),
                        new HttpConnectionFactory(httpsConf));
        httpsConnector.setPort(httpsPort);

        server.addConnector(httpsConnector);
        return server;
    }

    private static Resource findKeyStore(ResourceFactory resourceFactory,
            PathResourceFactory pathResourceFactory, String keystore) {

        log.info("Retrieving keystore from {}", keystore);

        Resource resource = resourceFactory.newClassLoaderResource(keystore);
        if (Resources.isReadableFile(resource)) {
            log.info("Read keystore from classpath");
            return resource;
        }

        resource = pathResourceFactory.newResource(keystore);

        if (Resources.isReadableFile(resource)) {
            log.info("Found keystore on file system");
            return resource;
        }

        throw new RuntimeException("Unable to read " + keystore);
    }

    public String serviceUrl() {
        return server.getURI().toString();
    }

    @Override
    public void close() throws Exception {
        String url = serviceUrl();
        log.info("Server stopped at {}", url);
        server.stop();
        server.destroy();
    }

    public int getServicePort() {
        return configuredPort;
    }
}
