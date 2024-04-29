package uk.org.edoatley.security.idp;

import java.util.Map;
import java.util.function.Supplier;
import java.util.HashMap;
import uk.org.edoatley.config.ConfigurationManager;
import uk.org.edoatley.security.idp.impl.DummyIdentityProvider;
import uk.org.edoatley.security.idp.impl.NoopIdentityProvider;

public class IdentityProviderFactory {
    private static Map<IdentityProviderEnum, IdentityProvider> identityProviderMap =
            new HashMap<>();



    public static synchronized IdentityProvider getIdentityProvider() {
        String idp = ConfigurationManager.get("service.identityProvider");
        IdentityProviderEnum idpEnum = IdentityProviderEnum.valueOf(idp);
        return getIdentityProvider(idpEnum);
    }

    public static synchronized IdentityProvider getIdentityProvider(
            IdentityProviderEnum identityProviderEnum) {
        switch (identityProviderEnum) {
            case NOOP:
                return singleton(identityProviderEnum, () -> new NoopIdentityProvider());
            case DUMMY:
                return singleton(identityProviderEnum, () -> new DummyIdentityProvider());
            default:
                throw new IllegalArgumentException("Unknown Identity Provider");
        }
    }

    private static synchronized IdentityProvider singleton(
            IdentityProviderEnum identityProviderEnum, Supplier<IdentityProvider> instantiator) {
        if (identityProviderMap.get(identityProviderEnum) == null) {
            identityProviderMap.put(identityProviderEnum, instantiator.get());
        }
        return identityProviderMap.get(identityProviderEnum);
    }
}
