package uk.org.edoatley.config;

import jakarta.ws.rs.core.Feature;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import jakarta.ws.rs.core.FeatureContext;
import uk.org.edoatley.security.idp.IdentityProvider;
import uk.org.edoatley.security.idp.impl.DummyIdentityProvider;

public class IdentityProviderFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(IdentityProvider.class).proxy(true).to(DummyIdentityProvider.class);
            }
        });
        return true;
    }
}
