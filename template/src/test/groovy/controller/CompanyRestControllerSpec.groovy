package controller

import pot.insurance.manager.dto.CompanyDTO
import base.TestableTrait
import pot.insurance.manager.controller.CompanyRestController
import pot.insurance.manager.service.CompanyService
import spock.lang.Specification

class CompanyRestControllerSpec extends Specification implements TestableTrait {

    def service = Mock(CompanyService)

    def controller = new CompanyRestController(service)

    def "expect createCompany method to return created DTO"() {
        when:
        service.saveCompany(_ as CompanyDTO) >> dto

        then:
        assertReceivedDataAreAsExpected(controller.createCompany(dto), dto)

        where:
        dto << [
            new CompanyDTO(UUID.randomUUID(), "Example company", "US", "example@gmail.com", "example.com", false)
        ]
    }

    def "expect getAllCompanies method to return the list of existing companies"() {
        given:
        List<CompanyDTO> companyDTOList = List.of(a, b)

        when:
        service.getAllCompanies() >> companyDTOList

        then:
        assertReceivedDataAreAsExpected(controller.getAllCompanies(), companyDTOList)

        where:
        a << [
            new CompanyDTO(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com", false)
        ]
        b << [
            new CompanyDTO(UUID.randomUUID(), "US", "Second company", "example2.com", "email2@gmail.com", false)
        ]
    }

    def "expect getCompanyById method to return the dto of the company with provided ID"() {
        when:
        service.getCompanyById(companyDTO.getId()) >> companyDTO

        then:
        assertReceivedDataAreAsExpected(controller.getCompanyById(companyDTO.getId()), companyDTO)

        where:
        companyDTO << [
            new CompanyDTO(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com", false)
        ]
    }

    def "expect deleteCompanyById method to return soft-deleted company dto with deletionStatus true "() {
        when:
        service.deleteCompanyById(companyDTO.getId()) >> companyDTO

        then:
        assertReceivedDataAreAsExpected(controller.deleteCompanyById(companyDTO.getId()), companyDTO)

        where:
        companyDTO << [
            new CompanyDTO(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com", true)
        ]
    }
}