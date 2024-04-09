package uk.org.edoatley.servlet.resources.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.Provider;
import uk.org.edoatley.servlet.model.Greeting;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import java.nio.charset.StandardCharsets;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GreetingMessageBodyWriter implements MessageBodyWriter<Greeting> {
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Greeting.class;
    }

    @Override
    public void writeTo(Greeting greeting, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out)
            throws IOException, WebApplicationException {
        out.write(greeting.getMessage().getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}