package uk.org.edoatley

import spock.lang.Specification

class HelloTest extends Specification {

    def "Application simple hello works"() {
        setup:
        def hello = new Hello()

        when:
        def result = hello.doGet

        then:
        result == "Howdy!"
    }
}
