package uk.org.edoatley;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import uk.org.edoatley.config.ITestConfiguration;

public class HelloITest {
    private static final Logger log = LoggerFactory.getLogger(HelloITest.class);
    private static final String API_HELLO = "/api/hello/";
    private String serviceUrl;

    @BeforeEach
    void setup() {
        ITestConfiguration testConfiguration = new ITestConfiguration();
        serviceUrl = testConfiguration.get("service.url");
        if (serviceUrl != null && serviceUrl.endsWith("/")) {
            serviceUrl = serviceUrl.substring(0, serviceUrl.length() - 1);
        }
    }

    // TODO:
    // 1. complete this https://github.com/Softeq/itest-gradle-plugin and get the
    // dependencies working o this test runs (and fails initially)
    // 2. fix the test so it passes
    // 3. Migrate the other tests to JUnit5
    // 4. Use the PropertiesReader class to read the configuration from the file in
    // the other places and remove ConfigurationManagement
    @Test
    void testHello() throws ClientProtocolException, IOException {
        // Given
        String name = RandomStringUtils.randomAlphabetic(8);
        String servicerUrl = serviceUrl + API_HELLO + name;
        log.info("serviceUrl: " + servicerUrl);
        HttpUriRequest request = new HttpGet(servicerUrl);
        request.setHeader("Content-Type", "application/json");

        // When
        CloseableHttpClient client = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = client.execute(request);

        // Then
        log.debug("Response: {}", httpResponse.toString());
        log.debug("Status Line: {}", httpResponse.getStatusLine().toString());
        log.debug("Entity: {}", httpResponse.getEntity().toString());

        assertEquals(HttpStatus.OK_200, httpResponse.getStatusLine().getStatusCode());
        assertEquals(new BasicResponseHandler().handleResponse(httpResponse), "Hello " + name);
    }
}