package uk.org.edoatley

import uk.org.edoatley.server.Jetty
import spock.lang.Specification
import spock.lang.Shared

class JettyServerSpecification extends Specification {

    @Shared String serviceUrl
    @Shared Jetty webapp

    def setupSpec() {
        println 'Setup spec'
        webapp = new Jetty(0)
        println 'Start server'
        //TODO: 
        webapp.startServer()
        println 'Start server done'
        // serviceUrl = webapp.serviceUrl()
        println 'Server url set'
    }

}
