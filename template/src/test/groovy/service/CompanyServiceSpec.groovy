package service

import base.TestableTrait
import org.springframework.dao.DataIntegrityViolationException
import pot.insurance.manager.entity.Company
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.repository.CompanyRepository
import pot.insurance.manager.dto.CompanyDTO
import pot.insurance.manager.mapper.CompanyMapper
import pot.insurance.manager.service.CompanyService
import spock.lang.Shared
import spock.lang.Specification

class CompanyServiceSpec extends Specification implements TestableTrait {

    @Shared
    CompanyService companyService
    CompanyMapper companyMapper = CompanyMapper.INSTANCE
    CompanyRepository companyRepository = Mock()

    def setup() {
        companyService = new CompanyService(companyRepository)
    }

    def "test for saveCompany method with correct data"() {
        when:
        companyRepository.findById(companyDTO.getId()) >> Optional.empty()
        companyRepository.save(_) >> companyMapper.companyDTOToCompany(companyDTO)

        then:
        assertReceivedDataAreAsExpected(companyService.saveCompany(companyDTO), companyDTO)
        notThrown(DataValidationException)

        where:
        companyDTO << [
                new CompanyDTO(UUID.randomUUID(), "US", "Example company", "example.com", "email@gmail.com")
        ]
    }

    def "test for saveCompany method when ID already exists"() {
        when:
        companyRepository.findById(_) >> conflictEntity
        companyRepository.save(companyMapper.companyDTOToCompany(companyDTO)) >> { throw new DataIntegrityViolationException("") }
        companyService.saveCompany(companyDTO)

        then:
        thrown(DataValidationException)

        where:
        conflictEntity << [
                Optional.of(new Company(UUID.randomUUID(), "US", "Example company", "example.com", "email@gmail.com")),
                Optional.empty()
        ]
        companyDTO << [
                new CompanyDTO(UUID.randomUUID(), "US", "Example company", "example.com", "email@gmail.com"),
                new CompanyDTO(UUID.randomUUID(), null, null, null, "email@gmail.com")
        ]

    }
}
