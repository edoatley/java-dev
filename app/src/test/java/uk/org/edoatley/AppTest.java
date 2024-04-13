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
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.SSLConfig;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import java.io.IOException;

public class AppTest {
    private static final String API_HELLO = "/api/hello";
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);
    private static final String TRUST_STORE = "tls/test-keystore.jks";
    private static final String TRUST_STORE_PASSWORD = "testjks";
    private static final String TEST_HOSTNAME = "restapi.edoatley.com";

    private static Jetty webapp;

    @BeforeAll
    public static void setUp() throws Exception {
        setupServer();
        setupRestAssured();
    }

    /**
     * Setup RestAssured to: use the truststore, direct requests to the local server and use the
     * test hostname header
     */
    private static void setupRestAssured() {

        RestAssured.config = RestAssured.config()
                .sslConfig(new SSLConfig().trustStore(TRUST_STORE, TRUST_STORE_PASSWORD));

        RestAssured.baseURI = "https://localhost:" + webapp.getServicePort();

        // @formatter:off
        RestAssured.requestSpecification = new RequestSpecBuilder()
            .build()
            .relaxedHTTPSValidation()
            .header("Host", TEST_HOSTNAME);
        // @formatter:on
    }

    /**
     * Start the local Jetty server
     * 
     * @throws IOException
     * @throws Exception
     */
    private static void setupServer() throws IOException, Exception {
        log.info("Setting up Jetty EMbedded Server");
        int freePort = NetworkUtil.nextFreePort();

        log.info("Starting the server on port {}", freePort);
        webapp = new Jetty(freePort, TRUST_STORE, TRUST_STORE_PASSWORD);
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
