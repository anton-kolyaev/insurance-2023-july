package controller

import pot.insurance.manager.dto.CompanyDTO
import base.TestableTrait
import pot.insurance.manager.controller.CompanyRestController
import pot.insurance.manager.service.CompanyService
import spock.lang.Specification

class CompanyRestControllerSpec extends Specification implements TestableTrait {
    def companyService = Mock(CompanyService)
    def controller = new CompanyRestController(companyService)

    def "test for createCompany method"() {
        when:
        companyService.saveCompany(_) >> companyDTO

        then:
        assertReceivedDataAreAsExpected(controller.createCompany(companyDTO), companyDTO)

        where:
        companyDTO << [
                new CompanyDTO(UUID.randomUUID(), "Example company", "US", "example@gmail.com", "example.com")
        ]
    }

    def "test for getAllCompanies method"() {
        given:
        List<CompanyDTO> companyDTOList = List.of(a, b)

        when:
        companyService.getAllCompanies() >> companyDTOList

        then:
        assertReceivedDataAreAsExpected(controller.getAllCompanies(), companyDTOList)

        where:
        a << [
                new CompanyDTO(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com")
        ]
        b << [
                new CompanyDTO(UUID.randomUUID(), "US", "Second company", "example2.com", "email2@gmail.com")
        ]
    }

    def "test for getCompanyById method"() {
        when:
        companyService.getCompanyById(companyDTO.getId()) >> companyDTO

        then:
        assertReceivedDataAreAsExpected(controller.getCompanyById(companyDTO.getId()), companyDTO)

        where:
        companyDTO << [
                new CompanyDTO(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com")
        ]
    }
}