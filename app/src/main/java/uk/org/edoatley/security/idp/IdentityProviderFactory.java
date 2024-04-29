package uk.org.edoatley.security.idp;

import java.util.Map;
import java.util.HashMap;
import uk.org.edoatley.security.idp.impl.DummyIdentityProvider;

public class IdentityProviderFactory {
    private static Map<IdentityProviderEnum, IdentityProvider> identityProviderMap =
            new HashMap<>();


    public static synchronized IdentityProvider getIdentityProvider(
            IdentityProviderEnum identityProviderEnum) {
        switch (identityProviderEnum) {
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
