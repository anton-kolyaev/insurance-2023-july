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

import java.sql.ClientInfoStatus

class CompanyServiceSpec extends Specification implements TestableTrait {

    @Shared
    CompanyService companyService
    CompanyMapper companyMapper = CompanyMapper.INSTANCE
    CompanyRepository companyRepository = Mock()

    def setup() {
        companyService = new CompanyService(companyRepository)
    }

    def "expect saveCompany method to return the saved dto without throwing exception"() {
        when:
        companyRepository.findById(companyDTO.getId()) >> Optional.empty()
        companyRepository.save(_) >> companyMapper.companyDTOToCompany(companyDTO)

        then:
        assertReceivedDataAreAsExpected(companyService.saveCompany(companyDTO), companyDTO)
        notThrown(DataValidationException)

        where:
        companyDTO << [
                new CompanyDTO(UUID.randomUUID(), "US", "Example company", "example.com", "email@gmail.com", false)
        ]
    }

    def "expect saveCompany method to throw an exception when company already exists"() {
        when:
        companyRepository.findById(_) >> conflictEntity
        companyRepository.save(companyMapper.companyDTOToCompany(companyDTO)) >> { throw new DataIntegrityViolationException("") }
        companyService.saveCompany(companyDTO)

        then:
        thrown(DataValidationException)

        where:
        conflictEntity << [
                Optional.of(new Company(UUID.randomUUID(), "US", "Example company", "example.com", "email@gmail.com", false)),
                Optional.empty()
        ]
        companyDTO << [
                new CompanyDTO(UUID.randomUUID(), "US", "Example company", "example.com", "email@gmail.com", false),
                new CompanyDTO(UUID.randomUUID(), null, null, null, "email@gmail.com", false)
        ]

    }

    def "expect getAllCompanies method to return the list of existing companies"() {
        given:
        List<Company> companyList = List.of(a, b)
        List<CompanyDTO> companyDTOList = companyList.stream().map(companyMapper::companyToCompanyDTO).toList()

        when:
        companyRepository.findAll() >> companyList

        then:
        assertReceivedDataAreAsExpected(companyService.getAllCompanies(), companyDTOList)

        where:
        a << [
                new Company(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com", false)
        ]
        b << [
                new Company(UUID.randomUUID(), "US", "Second company", "example2.com", "email2@gmail.com", false)
        ]
    }
}
