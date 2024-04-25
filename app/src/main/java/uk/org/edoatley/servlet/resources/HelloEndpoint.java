package uk.org.edoatley.servlet.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import uk.org.edoatley.security.Secured;
import uk.org.edoatley.servlet.model.Greeting;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloEndpoint {
    private static final Logger log = LoggerFactory.getLogger(HelloEndpoint.class);

    @Context
    private SecurityContext securityContext;

    @GET
    public Greeting hello() {
        log.info("Request to /hello");
        return new Greeting("Howdy!");
    }

    @GET
    @Secured
    @Path("/{param}")
    public Greeting hello(@PathParam("param") String name) {
        log.info("Request to /hello/{param}, param={}", name);
        log.info("SecurityContext is {}", securityContext);
        log.info("securityContext.getUserPrincipal() {}", securityContext.getUserPrincipal());
        log.info("The user requesting was {}", securityContext.getUserPrincipal().getName());
        return new Greeting("Hello " + name);
    }
}
