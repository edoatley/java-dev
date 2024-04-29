package uk.org.edoatley.security.idp;

import uk.org.edoatley.servlet.model.Credentials;

public interface IdentityProvider {
    void validateToken(String token) throws Exception;

    String lookupUser(String token);

    void authenticate(Credentials credentials) throws Exception;

    String issueToken(String username);
}
