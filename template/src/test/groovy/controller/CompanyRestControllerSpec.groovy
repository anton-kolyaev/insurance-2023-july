package controller

import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
import pot.insurance.manager.dto.CompanyDTO
import base.TestableTrait
import pot.insurance.manager.controller.CompanyRestController
import pot.insurance.manager.service.CompanyService
import spock.lang.Specification

class CompanyRestControllerSpec extends Specification implements TestableTrait {
    def companyService = Mock(CompanyService)
    def controller = new CompanyRestController(companyService)

    def "createCompany test"() {
        given:
        CompanyDTO companyDTO = a

        when:
        companyService.saveCompany(_) >> companyDTO

        then:
        assertReceivedDataAreAsExpected(controller.createCompany(companyDTO), companyDTO)

        where:
        a << [
                new CompanyDTO(UUID.randomUUID(), "Example company", "US", "example@gmail.com", "example.com")
        ]
    }

}
//    def "test createCompany API with correct data"() {
//        given:
//
//        def requestBody =
//                [
//                        "id"         : null,
//                        "companyName": a,
//                        "countryCode": b,
//                        "email"      : c,
//                        "site"       : d
//                ]
//        when:
//        def response = client.post {
//            request.uri.path = '/v1/companies'
//            request.body = requestBody
//        }
//        if (response.data) {
//            requestBody.id = response.data.id
//        }
//
//        then:
//        assertResponseStatus(response, e)
//        if (response == 200) {
//            requestBody == response.data
//        } else if (response == 500) {
//            requestBody != response.data
//        }
//        where:
//                a         |  b   |       c       |             d             |  e
//        "Example Company" | null |     null      | "https://www.example.com" | 500
//        "Example Company" | "US" | "example.com" | "https://www.example.com" | 201
//    }