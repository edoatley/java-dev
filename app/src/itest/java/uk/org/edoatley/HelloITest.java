package uk.org.edoatley;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import uk.org.edoatley.config.ITestConfiguration;

public class HelloITest {
    private static final Logger log = LoggerFactory.getLogger(HelloITest.class);
    private static final String API_HELLO = "/api/hello";
    private String serviceUrl;

    @BeforeEach
    void setup() {
        ITestConfiguration testConfiguration = new ITestConfiguration();
        serviceUrl = testConfiguration.get("service.url");
        if (serviceUrl != null && serviceUrl.endsWith("/")) {
            serviceUrl = serviceUrl.substring(0, serviceUrl.length() - 1);
        }
        log.info("Initialised HelloITest using the serviceUrl: {}", serviceUrl);
    }

    @Test
    @DisplayName("Call the /hello endpoint")
    public void testSimpleHello() {
        when()
                .get(serviceUrl + API_HELLO)
                .then()
                .statusCode(200)
                .contentType(equalTo("application/json"))
                .body("message", equalTo("Howdy!"));

    }

    @Test
    @DisplayName("Call the /api//hello endpoint with a name parameter")
    public void testNamedHello() {
        String name = RandomStringUtils.randomAlphabetic(8);
        when()
                .get(serviceUrl + API_HELLO + "/" + name)
                .then()
                .statusCode(200)
                .contentType(equalTo("application/json"))
                .body("message", equalTo("Hello " + name));
    }
}