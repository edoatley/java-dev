package uk.org.edoatley

import okhttp3.Response

class HelloTest extends JettyServerSpecification {

    def "Check service url set ok"() {
        expect:
        serviceUrl ==~ /http:\/\/.*/
    }

    def "Check webapp instantiated"() {
        expect:
        webapp != null
    }

    def "Application simple hello works"() {
        given:
            webapp != null && serviceUrl != null
        when:
            Response response = doGet('api/hello')
        then:
            response.code == 200
        and:
            jsonSlurp(response.body().string()).message == 'Howdy!'
    }

    def "Application named hello works"() {
        given:
            String name = 'Dave'
        when:
            Response response = doGet('api/hello/' + name)
        then:
            response.code == 200
        and:
            jsonSlurp(response.body().string()).message == 'Hello ' + name
    }

}
