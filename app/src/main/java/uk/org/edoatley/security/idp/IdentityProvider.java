package uk.org.edoatley.security.idp;

import org.glassfish.jersey.spi.Contract;
import uk.org.edoatley.servlet.model.Credentials;

@Contract
public interface IdentityProvider {
    void validateToken(String token) throws Exception;

    String lookupUser(String token);

    void authenticate(Credentials credentials) throws Exception;

    String issueToken(String username);
}
