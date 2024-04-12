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
import io.restassured.config.SSLConfig;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);
    private static final String TRUST_STORE = "tls/test-keystore.jks";
    private static final String TRUST_STORE_PASSWORD = "testjks";
    private static Jetty webapp;
    private static String serviceUrl;

    @BeforeAll
    public static void setUp() throws Exception {

        // Set up RestAssured to use the truststore
        RestAssured.config = RestAssured.config().sslConfig(new SSLConfig()
                .trustStore(TRUST_STORE, TRUST_STORE_PASSWORD));
        // Get port for the server
        int freePort = NetworkUtil.nextFreePort();

        log.info("Starting the server");
        webapp = new Jetty(freePort, TRUST_STORE, TRUST_STORE_PASSWORD);
        webapp.startServer(false);
        serviceUrl = webapp.serviceUrl();
        if (serviceUrl != null && serviceUrl.endsWith("/")) {
            serviceUrl = serviceUrl.substring(0, serviceUrl.length() - 1);
        }
        log.info("Server started at " + serviceUrl);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        log.info("Stopping the server");
        webapp.close();
    }

    @Test
    @DisplayName("Call the /hello endpoint")
    public void testSimpleHello() {
        // Need to fix SNI so we can remove the relaxedHTTPSValidation
        given().trustStore(TRUST_STORE, TRUST_STORE_PASSWORD).relaxedHTTPSValidation()
                .when()
                .get(serviceUrl + "/api/hello")
                .then()
                .statusCode(200)
                .contentType(equalTo("application/json"))
                .body("message", equalTo("Howdy!"));
    }

    @Test
    @DisplayName("Call the /hello endpoint with a name parameter")
    public void testNamedHello() {
        given().trustStore(TRUST_STORE, TRUST_STORE_PASSWORD)
                .when()
                .get(serviceUrl + "/api/hello/Bob")
                .then()
                .statusCode(200)
                .contentType(equalTo("application/json"))
                .body("message", equalTo("Hello Bob"));
    }
}
