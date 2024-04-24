package uk.org.edoatley.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import uk.org.edoatley.security.AuthenticationFilter;
import uk.org.edoatley.security.idp.IdentityProvider;
import uk.org.edoatley.security.idp.impl.DummyIdentityProvider;

public class JettyResourceConfig extends ResourceConfig {
    public JettyResourceConfig() {

        // add packages to scan for resources
        packages("uk.org.edoatley.servlet.resources");

        // Register Auth Filter here
        register(AuthenticationFilter.class);

        // register(AutoScanFeature.class);

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(DummyIdentityProvider.class).to(IdentityProvider.class);
            }
        });
    }
}
