package uk.org.edoatley.server;

import java.time.Duration;
import java.util.EventListener;

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

public class Jetty implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(Jetty.class);

    private int configuredPort;

    private Server server;

    public Jetty(int configuredPort) {
        this.configuredPort = configuredPort;
    }

    public void startServer(boolean blocking) throws Exception {

        log.debug("Starting server...");

        server = new Server(configuredPort);
        log.debug("Server instantiated...");
        server.setHandler(buildContextHandler());
        log.debug("Server context added...");

        log.debug("Attempting to start server");
        server.start();
        log.info("Server started at {}", serviceUrl());

        if (blocking) {
            log.info("Server.join() blocking server for {} until further notice", serviceUrl());
            server.join();
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

    @Override
    public void close() throws Exception {
        String url = serviceUrl();
        log.info("Server stopped at {}", url);
        server.stop();
        server.destroy();
    }
}
