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

    def "test for getAllComapnies method"() {
        given:
        List<Company> companyList = List.of(a, b)
        List<CompanyDTO> companyDTOList = companyList.stream().map(companyMapper::companyToCompanyDTO).toList()

        when:
        companyRepository.findAll() >> companyList

        then:
        assertReceivedDataAreAsExpected(companyService.getAllCompanies(), companyDTOList)

        where:
        a << [
                new Company(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com")
        ]
        b << [
                new Company(UUID.randomUUID(), "US", "Second company", "example2.com", "email2@gmail.com")
        ]
    }

    def "test for getCompanyById method with correct existing Id"() {
        when:
        companyRepository.findById(company.get().getId()) >> company

        then:
        assertReceivedDataAreAsExpected(companyService.getCompanyById(company.get().getId()), companyMapper.companyToCompanyDTO(company.get()))
        notThrown(DataValidationException)

        where:
        company << [
                Optional.of(new Company(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com"))
        ]
    }

    def "test for getCompanyById method with non-existing/incorrect Id"() {
        when:
        companyRepository.findById(id) >> Optional.empty()
        companyService.getCompanyById(id)

        then:
        thrown(DataValidationException)

        where:
        id << [
                UUID.randomUUID()
        ]
    }
}
