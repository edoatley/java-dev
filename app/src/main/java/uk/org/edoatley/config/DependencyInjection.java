package uk.org.edoatley.config;

import uk.org.edoatley.security.idp.IdentityProvider;
import uk.org.edoatley.security.idp.impl.DummyIdentityProvider;

public class DependencyInjection {
    private ConfigurationManager configurationManager;

    public DependencyInjection(ConfigurationManager cm) {
        this.configurationManager = cm;
    }

    public Class<? extends IdentityProvider> getIdentityProvider() {
        return DummyIdentityProvider.class;
    }
}
