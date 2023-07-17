package service

import base.TestableTrait
import org.springframework.dao.DataIntegrityViolationException
import pot.insurance.manager.entity.Company
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.repository.CompanyRepository
import pot.insurance.manager.dto.CompanyDTO
import pot.insurance.manager.mapper.CompanyMapper
import pot.insurance.manager.service.CompanyService
import pot.insurance.manager.service.CompanyServiceImpl
import spock.lang.Shared
import spock.lang.Specification

class CompanyServiceImplSpec extends Specification implements TestableTrait {

    @Shared
    CompanyService companyService = new CompanyServiceImpl(companyRepository)

    CompanyMapper companyMapper = CompanyMapper.INSTANCE

    CompanyRepository companyRepository = Mock()


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
        companyRepository.findAllByDeletionStatusFalse() >> companyList

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

    def "expect getCompanyById method to return the company with provided correct ID"() {
        when:
        companyRepository.findByIdAndDeletionStatusFalse(company.get().getId()) >> company

        then:
        assertReceivedDataAreAsExpected(companyService.getCompanyById(company.get().getId()), companyMapper.companyToCompanyDTO(company.get()))
        notThrown(DataValidationException)

        where:
        company << [
                Optional.of(new Company(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com", false))
        ]
    }

    def "expect getCompanyById method to throw an exception when the company with provided ID is non-existent"() {
        when:
        companyRepository.findByIdAndDeletionStatusFalse(id) >> Optional.empty()
        companyService.getCompanyById(id)

        then:
        thrown(DataValidationException)

        where:
        id << [
                UUID.randomUUID()
        ]
    }

    def "expect deleteCompanyById method to return the dto with the deletionStatus set to true"() {
        when:
        companyRepository.findByIdAndDeletionStatusFalse(company.get().getId()) >> company
        companyRepository.save(_ as Company) >> company.get()

        then:
        assertReceivedDataAreAsExpected(companyService.deleteCompanyById(company.get().getId()).isDeletionStatus(), true)
        notThrown(DataValidationException)

        where:
        company << [
                Optional.of(new Company(UUID.randomUUID(), "US", "First company", "example1.com", "email1@gmail.com", false))
        ]
    }

    def "expect deleteCompanyById method to throw an exception when company is not found"() {
        when:
        companyRepository.findByIdAndDeletionStatusFalse(id) >> Optional.empty()
        companyService.deleteCompanyById(id)

        then:
        thrown(DataValidationException)

        where:
        id << [
                UUID.randomUUID()
        ]
    }
}
