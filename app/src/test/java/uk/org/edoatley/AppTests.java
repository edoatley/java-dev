package uk.org.edoatley;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

/**
 * This test runs against the local Jetty server where TLS has not been configured to prove that the
 * application is working as expected over HTTP.
 */
public abstract class AppTests {
    private static final String API_HELLO = "/api/hello";
    private static final String API_AUTHN = "/api/authentication";
    private static final String VALID_USER_AUTHN_BODY = """
            {
                "username": "user",
                "password": "password"
            }
            """;

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

    @Test
    @DisplayName("Authenticate a user successfully")
    public void testAuthN() {
        login();
    }

    @Test
    @DisplayName("Fail to Authenticate a user as password wrong")
    public void testAuthNWrongPassword() {
        String authenticationBody = """
                {
                "username": "user",
                "password": "password1"
                }
                """;
        loginFailsWithStatus(authenticationBody, 403);
    }

    @Test
    @DisplayName("Fail to Authenticate a invalid user ")
    public void testAuthNUnknownUser() {
        String authenticationBody = """
                {
                "username": "user1",
                "password": "password1"
                }
                """;
        loginFailsWithStatus(authenticationBody, 403);
    }

    // Utility methods

    private void loginFailsWithStatus(String authenticationBody, int status) {
        // @formatter:off
        given().
            body(authenticationBody).
            accept(ContentType.JSON).
            contentType(ContentType.JSON).
        when().
            post(API_AUTHN).
        then().
            statusCode(status).
            contentType(equalTo("application/json"));
    }

    private void login() {
        // @formatter:off
        given().
            body(VALID_USER_AUTHN_BODY).
            accept(ContentType.JSON).
            contentType(ContentType.JSON).
        when().
            post(API_AUTHN).
        then().
            statusCode(200).
            contentType(equalTo("application/json"));
    }
}
