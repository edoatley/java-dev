package uk.org.edoatley.server;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.edoatley.resource.HelloResource;
import uk.org.edoatley.resource.writer.GreetingMessageBodyWriter;

public class Jetty {
    private static final Logger log = LoggerFactory.getLogger(Jetty.class);

    private int configuredPort;

    private Server server;

    public Jetty(int configuredPort) {
        this.configuredPort = configuredPort;
    }

    public void startServer() throws Exception {

        log.debug("Starting server...");

        server = new Server(configuredPort);
        server.setHandler(buildContextHandler());

        try {
            log.debug("Attempting to start server");
            server.start();
            log.debug("Attempting to join server");
            server.join();
            log.info("Server started at {0}", serviceUrl());
        } finally {
            log.info("Server destruction triggered");
            server.destroy();
        }
    }

    private Handler buildContextHandler() {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(HelloResource.class);
        resourceConfig.register(GreetingMessageBodyWriter.class);
        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/api/*");
        log.debug("Handler created");
        return handler;
    }

    public String serviceUrl() {
        return server.getURI().toString();
    }

}
