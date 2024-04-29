package uk.org.edoatley.servlet.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.org.edoatley.security.idp.IdentityProvider;
import uk.org.edoatley.security.idp.IdentityProviderEnum;
import uk.org.edoatley.security.idp.IdentityProviderFactory;
import uk.org.edoatley.servlet.model.Credentials;

@Path("/authentication")
public class AuthenticationEndpoint {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationEndpoint.class);

    private IdentityProvider identityProvider = IdentityProviderFactory.getIdentityProvider(IdentityProviderEnum.DUMMY);

    /**
     * Takes payload that looks like this:
     * 
     * { "username": "admin", "password": "123456" }
     * 
     * @param credentials - see json above
     * @return response containing token
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {

        try {

            log.debug("Authentication attempt for {}", credentials.username());
            // Authenticate the user using the credentials provided
            identityProvider.authenticate(credentials);

            // Issue a token for the user

            log.debug("Issuing token for {}", credentials.username());
            String token = identityProvider.issueToken(credentials.username());

            // Return the token on the response
            log.debug("Returning token for {}", credentials.username());
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
