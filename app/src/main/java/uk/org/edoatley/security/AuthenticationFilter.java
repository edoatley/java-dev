package uk.org.edoatley.security;

import java.io.IOException;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import uk.org.edoatley.security.idp.IdentityProvider;
import uk.org.edoatley.security.idp.IdentityProviderFactory;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String REALM = "edoatley";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    private IdentityProvider identityProvider = IdentityProviderFactory.getIdentityProvider();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("Filtering the request");
        // Get the Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        log.info("Authorization header is {}", authorizationHeader);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            log.info("Request is not token based");
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
        log.info("Extracted token is {} ", token);

        try {

            // Validate the token
            log.info("Validating the token");
            identityProvider.validateToken(token);
            log.info("Token is valid");
            setCurrentUserContext(requestContext, token);
            log.info("Set the current user context");

        } catch (Exception e) {
            log.error("Token validation failed", e);
            abortWithUnauthorized(requestContext);
        }
    }

    private void setCurrentUserContext(ContainerRequestContext requestContext, String token) {
        log.info("Setting the security context for the user from token {}", token);
        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {
                return () -> identityProvider.lookupUser(token);
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return AUTHENTICATION_SCHEME;
            }
        });
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext
                .abortWith(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .header(HttpHeaders.WWW_AUTHENTICATE,
                                        AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                                .build());
    }
}
