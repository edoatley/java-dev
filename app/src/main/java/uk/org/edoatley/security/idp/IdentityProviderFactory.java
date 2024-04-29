package uk.org.edoatley.security.idp;

import java.util.Map;
import jakarta.validation.Configuration;
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
                if (identityProviderMap.get(identityProviderEnum) == null) {
                    identityProviderMap.put(identityProviderEnum, new NoopIdentityProvider());
                }
                return identityProviderMap.get(identityProviderEnum);
            case DUMMY:
                if (identityProviderMap.get(identityProviderEnum) == null) {
                    identityProviderMap.put(identityProviderEnum, new DummyIdentityProvider());
                }
                return identityProviderMap.get(identityProviderEnum);
            // case AUTH0:
            // return new Auth0IdentityProvider();
            // case ENTRA_ID:
            // return new EntraIdIdentityProvider();
            // case AWS:
            // return new AwsIdentityProvider();
            default:
                throw new IllegalArgumentException("Unknown Identity Provider");
        }
    }


}
