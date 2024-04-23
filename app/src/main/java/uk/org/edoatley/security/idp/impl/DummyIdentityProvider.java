package uk.org.edoatley.security.idp.impl;

import java.util.Map;
import java.util.HashMap;
import uk.org.edoatley.security.idp.IdentityProvider;
import uk.org.edoatley.servlet.model.Credentials;

public class DummyIdentityProvider implements IdentityProvider {

    Map<String, String> users = new HashMap<>();
    Map<String, String> tokens = new HashMap<>();

    public DummyIdentityProvider() {
        users.put("admin", "123456");
        tokens.put("admin", "dummytoken");
        users.put("user", "password");
        tokens.put("user", "specialtoken");
    }

    @Override
    public void validateToken(String token) throws Exception {
        if (!tokens.containsValue(token)) {
            throw new Exception("Invalid token");
        }
    }

    @Override
    public String lookupUser(String token) {
        return tokens.entrySet().stream().filter(entry -> entry.getValue().equals(token))
                .map(Map.Entry::getKey).findFirst().orElse(null);
    }

    @Override
    public void authenticate(Credentials credentials) throws Exception {
        // get the map entry for the username and check the password
        String password = users.get(credentials.username());
        if (password == null || !password.equals(credentials.password())) {
            throw new Exception("Invalid credentials");
        }
    }

    @Override
    public String issueToken(String username) {
        return tokens.get(username);
    }


}
