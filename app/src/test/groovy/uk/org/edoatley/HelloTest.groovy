package uk.org.edoatley

import groovyx.net.http.RESTClient

class HelloTest extends JettyServerSpecification {

    def "Application simple hello works"() {
        given:
            webapp != null && serviceUrl != null
        println(serviceUrl)
        when:
        println('Calling url ' + serviceUrl + '/api/hello')
        response = new RESTClient(serviceUrl).get(path : '/api/hello')
        then:
        println('Checking response ' + response)
        response.status == 200
        and:
            response.data == 'Howdy!'
    }

}
