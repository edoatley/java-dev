package uk.org.edoatley;

import uk.org.edoatley.server.Jetty;
import uk.org.edoatley.utils.NetworkUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import java.io.IOException;

/**
 * This test runs against the local Jetty server where TLS has been configured to prove that the
 * application is working as expected over HTTPS.
 */
public class AppSecureTest extends AppTests {

    private static final Logger log = LoggerFactory.getLogger(AppSecureTest.class);
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

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
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
}
