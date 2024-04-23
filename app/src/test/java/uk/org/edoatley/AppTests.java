package uk.org.edoatley;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

/**
 * This test runs against the local Jetty server where TLS has not been configured to prove that the
 * application is working as expected over HTTP.
 */
public abstract class AppTests {
    private static final String API_HELLO = "/api/hello";

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
        login();

        given().
            header("Authorization", "Bearer specialtoken").
        when().
            get(API_HELLO + "/Bob").
        then().
            statusCode(200).
            contentType(equalTo("application/json")).
            body("message", equalTo("Hello Bob"));
        // @formatter:on
    }

    private void login() {
        String authenticationBody = """
                {
                    "username": "user",
                    "password": "password"
                }
                """;
        // @formatter:off
        given().
            body(authenticationBody).
        when().
            post("/authentication").
        then().
            statusCode(200).
            contentType(equalTo("application/json"));
    }
}
