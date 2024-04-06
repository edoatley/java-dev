/* groovylint-disable ClassJavadoc */
package uk.org.edoatley

import uk.org.edoatley.server.Jetty

import spock.lang.Specification
import spock.lang.Shared
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
  Base spec class to control the Jetty server we are starting for our tests
**/
@CompileStatic
class JettyServerSpecification extends Specification {

  @Shared String serviceUrl
  @Shared Jetty webapp
  @Shared Logger log

  /* groovylint-disable-next-line MethodReturnTypeRequired, NoDef */
  def setupSpec() {
    log = LoggerFactory.getLogger(JettyServerSpecification)

    webapp = new Jetty(0)
    log.info('setupSpec() - Jetty instantiated')

    webapp.startServer(false)
    log.info('setupSpec() - Jetty server started')

    serviceUrl = webapp.serviceUrl()
    log.info('setupSpec() - serviceUrl={}', serviceUrl)
  }

}
