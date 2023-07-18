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

import spock.lang.Specification
import spock.lang.Subject

class CompanyServiceImplSpec extends Specification implements TestableTrait {

    CompanyMapper mapper = CompanyMapper.INSTANCE

    CompanyRepository repository = Mock()

    @Subject
    CompanyService service = new CompanyServiceImpl(repository)


    def "expect saveCompany method to return the saved dto without throwing exception"() {
        when:
        repository.findById(dto.getId()) >> Optional.empty()
        repository.save(_ as Company) >> mapper.toEntity(dto)

        then:
        assertReceivedDataAreAsExpected(service.saveCompany(dto), dto)
        notThrown(DataValidationException)

        where:
        dto << [
                new CompanyDTO(UUID.randomUUID(), "US", "Example entity", "example.com", "email@gmail.com", false)
        ]
    }

    def "expect saveCompany method to throw an exception when company already exists"() {
        when:
        repository.findById(_ as UUID) >> entity
        repository.save(mapper.toEntity(dto)) >> { throw new DataIntegrityViolationException("") }
        service.saveCompany(dto)

        then:
        thrown(DataValidationException)

        where:
        entity << [
                Optional.of(new Company(UUID.randomUUID(), "US", "Example entity", "example.com", "email@gmail.com", false)),
                Optional.empty()
        ]
        dto << [
                new CompanyDTO(UUID.randomUUID(), "US", "Example entity", "example.com", "email@gmail.com", false),
                new CompanyDTO(UUID.randomUUID(), null, null, null, "email@gmail.com", false)
        ]

    }

    def "expect getAllCompanies method to return the list of existing companies"() {
        given:
        List<Company> entities = List.of(a, b)
        List<CompanyDTO> dtos = entities.stream().map(mapper::companyToCompanyDTO).toList()

        when:
        repository.findAllByDeletionStatus(false) >> entities

        then:
        assertReceivedDataAreAsExpected(service.getAllCompanies(), dtos)

        where:
        a << [
            new Company(UUID.randomUUID(), "US", "First entity", "example1.com", "email1@gmail.com", false)
        ]
        b << [
            new Company(UUID.randomUUID(), "US", "Second entity", "example2.com", "email2@gmail.com", false)
        ]
    }

    def "expect getCompanyById method to return the company with provided correct ID"() {
        when:
        repository.findByIdAndDeletionStatus(company.get().getId(), false) >> company

        then:
        assertReceivedDataAreAsExpected(service.getCompanyById(company.get().getId()), mapper.companyToCompanyDTO(company.get()))
        notThrown(DataValidationException)

        where:
        company << [
            Optional.of(new Company(UUID.randomUUID(), "US", "First entity", "example1.com", "email1@gmail.com", false))
        ]
    }

    def "expect getCompanyById method to throw an exception when the company with provided ID is non-existent"() {
        when:
        repository.findByIdAndDeletionStatus(id, false) >> Optional.empty()
        service.getCompanyById(id)

        then:
        thrown(DataValidationException)

        where:
        id << [
            UUID.randomUUID()
        ]
    }

    def "expect deleteCompanyById method to return the dto with the deletionStatus set to true"() {
        when:
        repository.findByIdAndDeletionStatus(entity.get().getId(), false) >> entity
        repository.save(_ as Company) >> entity.get()

        then:
        assertReceivedDataAreAsExpected(service.deleteCompanyById(entity.get().getId()).isDeletionStatus(), true)
        notThrown(DataValidationException)

        where:
        entity << [
            Optional.of(new Company(UUID.randomUUID(), "US", "First entity", "example1.com", "email1@gmail.com", false))
        ]
    }

    def "expect deleteCompanyById method to throw an exception when company is not found"() {
        when:
        repository.findByIdAndDeletionStatus(id, false) >> Optional.empty()
        service.deleteCompanyById(id)

        then:
        thrown(DataValidationException)

        where:
        id << [
            UUID.randomUUID()
        ]
    }
}
