package uk.org.edoatley

import spock.lang.Specification

class SimpleSpockTest extends Specification {

    def "Addition works"() {
        when:
        def result = 2 + 3
        then:
             result == 5
    }

}
