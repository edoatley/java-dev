package uk.org.edoatley.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.edoatley.security.AuthenticationFilter;

public class JettyResourceConfig extends ResourceConfig {
    private static final Logger log = LoggerFactory.getLogger(JettyResourceConfig.class);

    public JettyResourceConfig() {

        log.info("Setting the packages");
        // add packages to scan for resources
        packages("uk.org.edoatley.servlet.resources");

        log.info("Setting the auth filter");
        // Register Auth Filter here
        register(AuthenticationFilter.class);
    }
}
