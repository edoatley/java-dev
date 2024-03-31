// package uk.org.edoatley

// import groovyx.net.http.RESTClient

// class HelloTest extends JettyServerSpecification {

//     def "Application simple hello works"() {
//         given:
//             webapp != null && serviceUrl != null
//         println(serviceUrl)
//         when:
//             response = new RESTClient(serviceUrl).get(path : '/hello')
//         then:
//              response.status == 200
//         and:
//             response.data == 'Howdy!'
//     }

// }
