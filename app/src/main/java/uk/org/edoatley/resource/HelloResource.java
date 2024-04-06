package uk.org.edoatley.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import uk.org.edoatley.model.Greeting;

public class HelloResource {
    private static final Logger log = LoggerFactory.getLogger(HelloResource.class);

    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting hello() {
        log.info("Request to /hello");
        return new Greeting("Howdy!");
    }

    @GET
    @Path("/hello/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting hello(@PathParam("param") String name) {
        log.info("Request to /hello/{param}, param={}", name);
        return new Greeting("Hello " + name);
    }
}
