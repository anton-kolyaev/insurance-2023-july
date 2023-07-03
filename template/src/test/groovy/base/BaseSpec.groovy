package base

import groovyx.net.http.HttpBuilder
import spock.lang.Shared
import spock.lang.Specification

class BaseSpec extends Specification {

    @Shared
    public static HttpBuilder client

    final static String target = System.getProperty('target') ?: 'http://localhost:8080'

    def setupSpec() {
        client = HttpBuilder.configure {
            request.uri = target
            request.setContentType('application/json')
            response.success { resp, data ->
                if(data)
                    [
                            data: data,
                            status: resp.statusCode
                    ]
                else
                    [
                            status: resp.statusCode
                    ]
            }
            response.failure { resp, data ->
                [
                        stacktrace: data instanceof byte[] ? new String(data) : data,
                        status: resp.statusCode
                ]
            }
        }
    }
}
