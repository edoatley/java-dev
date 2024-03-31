package uk.org.edoatley.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@WebServlet(name = "HelloServlet", urlPatterns = { "hello" }, loadOnStartup = 1)
public class HelloServlet
        extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(HelloServlet.class);
    public static final String CONTEXT_PATH = "/hello";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.debug("Recieved request");
        response.getWriter().print("Howdy");
    }
}
