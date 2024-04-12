package uk.org.edoatley;

import uk.org.edoatley.server.Jetty;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class AppTest {
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);
    static Jetty webapp;
    static String serviceUrl;

    @BeforeAll
    public static void setUp() throws Exception {
        log.info("Starting the server");
        webapp = new Jetty(0);
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
        given()
                .relaxedHTTPSValidation()
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
        given()
                .relaxedHTTPSValidation()
                .when()
                .get(serviceUrl + "/api/hello/Bob")
                .then()
                .statusCode(200)
                .contentType(equalTo("application/json"))
                .body("message", equalTo("Hello Bob"));
    }
}
