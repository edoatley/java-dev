package uk.org.edoatley.security.idp.impl;

import uk.org.edoatley.security.idp.IdentityProvider;
import uk.org.edoatley.servlet.model.Credentials;

public class NoopIdentityProvider implements IdentityProvider {


    @Override
    public void validateToken(String token) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateToken'");
    }

    @Override
    public String lookupUser(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lookupUser'");
    }

    @Override
    public void authenticate(Credentials credentials) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticate'");
    }

    @Override
    public String issueToken(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'issueToken'");
    }

}
