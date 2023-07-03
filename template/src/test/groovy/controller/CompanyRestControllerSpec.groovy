package controller

import base.BaseSpec
import base.TestableTrait

class CompanyRestControllerSpec extends BaseSpec implements TestableTrait {

    def "test createCompany API with correct data"() {
        given:

        def requestBody =
        [
                "id": null,
                "companyName": "Example Company",
                "countryCode": "US",
                "email": "example@example.com",
                "site": "https://www.example.com"
        ]

        when:
        def response = client.post {
            request.uri.path = '/v1/companies'
            request.body = requestBody
        }
        if(response.data) {
            requestBody.id = response.data.id
        }

        then:
        assertResponseStatus(response, 201)
        requestBody == response.data
    }

    def "test createCompany API with incorrect data"() {
        given:

        def requestBody =
                [
                        "id": null,
                        "companyName": "Example Company",
                        "countryCode": null,
                        "email": null,
                        "site": "https://www.example.com"
                ]

        when:
        def response = client.post {
            request.uri.path = '/v1/companies'
            request.body = requestBody
        }
        if(response.data) {
            requestBody.id = response.data.id
        }

        then:
        assertResponseStatus(response, 500)
        requestBody != response.data
    }
}
