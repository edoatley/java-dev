package uk.org.edoatley;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import uk.org.edoatley.security.AuthenticationFilter;
import uk.org.edoatley.security.idp.IdentityProvider;
import uk.org.edoatley.security.idp.impl.DummyIdentityProvider;

public class TestResourceConfig extends ResourceConfig {
    public TestResourceConfig() {

        // add packages to scan for resources
        packages("uk.org.edoatley.servlet.resources");

        // Register Auth Filter here
        register(AuthenticationFilter.class);

        // Register the other classes
        register(new AbstractBinder() {
            @Override
            protected void configure() {

                bind(IdentityProvider.class).to(DummyIdentityProvider.class);
            }
        });
    }
}
