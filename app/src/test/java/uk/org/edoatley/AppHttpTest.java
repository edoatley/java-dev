package uk.org.edoatley;

import uk.org.edoatley.server.Jetty;
import uk.org.edoatley.utils.NetworkUtil;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import java.io.IOException;

/**
 * This test runs against the local Jetty server where TLS has not been configured to prove that the
 * application is working as expected over HTTP.
 */
public class AppHttpTest {
    private static final String API_HELLO = "/api/hello";
    private static final Logger log = LoggerFactory.getLogger(AppHttpTest.class);

    private static Jetty webapp;

    @BeforeAll
    public static void setUp() throws Exception {
        setupHttpServer();
        RestAssured.baseURI = "http://localhost:" + webapp.getServicePort();
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

    @Test
    @DisplayName("Call the /hello endpoint")
    public void testSimpleHello() {
        // @formatter:off
        when().
            get(API_HELLO).
        then().
            statusCode(200).
            contentType(equalTo("application/json")).
            body("message", equalTo("Howdy!"));
        // @formatter:on
    }

    @Test
    @DisplayName("Call the /hello endpoint with a name parameter")
    public void testNamedHello() {
        // @formatter:off
        when().
            get(API_HELLO + "/Bob").
        then().
            statusCode(200).
            contentType(equalTo("application/json")).
            body("message", equalTo("Hello Bob"));
        // @formatter:on
    }
}
