package controller

import pot.insurance.manager.dto.CompanyDTO
import base.TestableTrait
import pot.insurance.manager.controller.CompanyRestController
import pot.insurance.manager.service.CompanyService
import spock.lang.Specification

class CompanyRestControllerSpec extends Specification implements TestableTrait {
    def companyService = Mock(CompanyService)
    def controller = new CompanyRestController(companyService)

    def "createCompany test"() {
        when:
        companyService.saveCompany(_) >> a

        then:
        assertReceivedDataAreAsExpected(controller.createCompany(a), a)

        where:
        a << [
                new CompanyDTO(UUID.randomUUID(), "Example company", "US", "example@gmail.com", "example.com")
        ]
    }
}