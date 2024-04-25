package uk.org.edoatley;

import uk.org.edoatley.server.Jetty;
import uk.org.edoatley.utils.NetworkUtil;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import java.io.IOException;

/**
 * This test runs against the local Jetty server where TLS has not been configured to prove that the
 * application is working as expected over HTTP.
 */
public class AppHttpTest extends AppTests {
    private static final Logger log = LoggerFactory.getLogger(AppHttpTest.class);

    private static Jetty webapp;

    @BeforeAll
    public static void setUp() throws Exception {
        setupHttpServer();
        RestAssured.baseURI = "http://localhost:" + webapp.getServicePort();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    /**
     * Start the local Jetty server
     * 
     * @throws IOException
     * @throws Exception
     */
    private static void setupHttpServer() throws IOException, Exception {
        log.info("Setting up Jetty EMbedded Server");
        int freePort = NetworkUtil.nextFreePort();

        log.info("Starting the server on port {}", freePort);
        webapp = new Jetty(freePort);
        webapp.startServer(false);
    }

    /**
     * Stop the local Jetty server
     * 
     * @throws Exception
     */
    @AfterAll
    public static void tearDown() throws Exception {
        log.info("Stopping the server");
        webapp.close();
    }
}
