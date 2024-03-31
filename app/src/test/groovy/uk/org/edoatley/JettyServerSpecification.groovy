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
        webapp.startServer()
        serviceUrl = webapp.serviceUrl()
    }

    def cleanupSpec() {
        println 'Cleanup spec'
        webapp.close()
    }

}
