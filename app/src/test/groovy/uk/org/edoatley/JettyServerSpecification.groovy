package uk.org.edoatley

import uk.org.edoatley.server.Jetty

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

import spock.lang.Specification
import spock.lang.Shared

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.json.JsonSlurper

/**
  Base spec class to control the Jetty server we are starting for our tests
**/
class JettyServerSpecification extends Specification {

  @Shared String serviceUrl
  @Shared Jetty webapp
  @Shared Logger log
  @Shared OkHttpClient client

  def setupSpec() {
    log = LoggerFactory.getLogger(JettyServerSpecification)

    log.info('setupSpec() - Groovy Version: {}', GroovySystem.version)
    webapp = new Jetty(0)
    log.info('setupSpec() - Jetty instantiated')

    webapp.startServer(false)
    log.info('setupSpec() - Jetty server started')

    serviceUrl = webapp.serviceUrl()
    log.info('setupSpec() - serviceUrl={}', serviceUrl)

    client = new OkHttpClient()
  }

  def cleanupSpec() {
    webapp.close()
  }

  def Response doGet(String path) {
    Request getRequest = new Request.Builder()
      .url(serviceUrl + path)
      .addHeader('Content-Type', 'application/json').build()
    Response getResponse = client.newCall(getRequest).execute()
    return getResponse
  }

  def jsonSlurp(String jsonContent) {
    return new JsonSlurper().parseText(jsonContent)
  }

}
