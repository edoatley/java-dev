package uk.org.edoatley.server;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.edoatley.servlet.HelloServlet;

public class Jetty implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(Jetty.class);

    private int configuredPort;
    private String contextPath;

    private Server server;

    public Jetty(int configuredPort, String contextPath) {
        this.configuredPort = configuredPort;
        this.contextPath = contextPath;
    }

    public void startServer() throws Exception {

        log.debug("Starting server...");

        server = new Server(configuredPort);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setContextPath(contextPath);
        context.addServlet(HelloServlet.class, HelloServlet.CONTEXT_PATH);

        server.start();
        server.join();
        log.info("Server started at {0}", serviceUrl());
    }

    public void close() throws Exception {
        stopServer();
    }

    private void stopServer() throws Exception {
        server.stop();
    }

    public String serviceUrl() {
        return server.getURI().toString();
    }

}
