package service

import base.TestableTrait
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

    def "testing saveCompany method"() {
        given:
        CompanyRepository companyRepository = Stub()

        CompanyDTO companyDTO = CompanyDTO.builder()
            .id(UUID.randomUUID())
            .countryCode("US")
            .companyName("Example company")
            .site("example.com")
            .email("email@gmail.com")
            .build()

        companyService = new CompanyService(companyRepository)
        companyRepository.save(_) >> companyMapper.companyDTOToCompany(companyDTO)

        expect:
        assertReceivedDataAreAsExpected(companyService.saveCompany(companyDTO), companyDTO)
    }
}
