package uk.org.edoatley.config;

import org.glassfish.jersey.server.ResourceConfig;
import uk.org.edoatley.security.AuthenticationFilter;

public class JettyResourceConfig extends ResourceConfig {
    public JettyResourceConfig() {

        // add packages to scan for resources
        packages("uk.org.edoatley.servlet.resources");

        // Register Auth Filter here
        register(AuthenticationFilter.class);
    }
}
