package uk.org.edoatley;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import uk.org.edoatley.config.ITestConfiguration;

public class HelloITest {
    private static final Logger log = LoggerFactory.getLogger(HelloITest.class);
    private static final String API_HELLO = "/api/hello";

    /**
     * Setup RestAssured to: use the truststore, direct requests to the local server and use the
     * test hostname header
     */
    @BeforeAll
    static void setupRestAssured() {
        ITestConfiguration testConfiguration = new ITestConfiguration();

        String truststore = testConfiguration.get("server.keystore");
        String truststorePassword = testConfiguration.get("server.keystore.password");
        int servicePort = Integer.parseInt(testConfiguration.get("server.port"));
        String testHostname = testConfiguration.get("server.hostname");
        log.info(
                "Using the following configuration: truststore={}, servicePort={}, testHostname={}",
                truststore, servicePort, testHostname);

        RestAssured.config = RestAssured.config()
                .sslConfig(new SSLConfig().trustStore(truststore, truststorePassword));

        RestAssured.baseURI = "https://localhost:" + servicePort;

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        // @formatter:off
        RestAssured.requestSpecification = new RequestSpecBuilder()
            .build()
            .relaxedHTTPSValidation()
            .header("Host", testHostname);
        // @formatter:on
    }


    @Test
    @DisplayName("Call the /api/hello endpoint")
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
    @DisplayName("Call the /api/hello endpoint with a name parameter")
    public void testNamedHello() {
        String name = RandomStringUtils.randomAlphabetic(8);
        // @formatter:off
        when().
            get(API_HELLO + "/" + name).
        then().
            statusCode(200).
            contentType(equalTo("application/json")).
            body("message", equalTo("Hello " + name));
        // @formatter:on
    }
}
