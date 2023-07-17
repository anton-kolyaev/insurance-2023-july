package service

import org.springframework.dao.DataIntegrityViolationException

import pot.insurance.manager.dto.CompanyFunctionsDTO
import pot.insurance.manager.entity.CompanyFunctions
import pot.insurance.manager.exception.DataValidationException

import pot.insurance.manager.mapper.CompanyFunctionsMapper
import pot.insurance.manager.repository.CompanyFunctionsRepository
import pot.insurance.manager.repository.CompanyRepository
import pot.insurance.manager.service.CompanyFunctionsService
import pot.insurance.manager.entity.Company
import pot.insurance.manager.service.CompanyFunctionsServiceImpl
import spock.lang.Specification
import spock.lang.Shared
import spock.lang.Unroll

class CompanyFunctionsServiceSpec extends Specification{

    
    CompanyRepository companyRepository = Mock()
    CompanyFunctionsRepository companyFunctionsRepository = Mock()
    CompanyFunctionsMapper companyFunctionsMapper = CompanyFunctionsMapper.INSTANCE

    @Shared
    CompanyFunctionsService companyFunctionsService = new CompanyFunctionsServiceImpl(companyRepository, companyFunctionsRepository)

    @Unroll
    def "test saveCompanyFunctions - successful case"() {
        given:
            def companyId = UUID.randomUUID()
            def companyFunctionsDTO = new CompanyFunctionsDTO(
                id: null,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            )
        
        when:
            companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            companyFunctionsRepository.save(_ as CompanyFunctions) >> companyFunctionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO)
            def result = companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO)

        then:
            result != null
            result == companyFunctionsDTO
            notThrown(DataValidationException)
        and:
            1 * companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            1 * companyFunctionsRepository.save(_ as CompanyFunctions) >> companyFunctionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO)
    }


    @Unroll
    def "test saveCompanyFunctions shoul throw exception when company is not found"() {
        given:
            UUID companyId = UUID.randomUUID()
            def companyFunctionsDTO = new CompanyFunctionsDTO(
                id: null,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            )

        when:
            companyRepository.findByIdAndDeletionStatusFalse(_) >> Optional.empty()
            companyFunctionsRepository.save(_ as CompanyFunctions) >> companyFunctionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO)
            def result = companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO)

        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
        
        and:
            1 * companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.empty()
            0 * companyFunctionsRepository.save(_ as CompanyFunctions) >> companyFunctionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO)
    
    }

    @Unroll
    def "test saveCompanyFunctions shoul throw exception when data is malformed"() {
        given:
            UUID companyId = UUID.randomUUID()
            CompanyFunctionsDTO companyFunctionsDTO = new CompanyFunctionsDTO()
        
        when:
            companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            companyFunctionsRepository.save(_ as CompanyFunctions) >> { throw new DataIntegrityViolationException("") }
            def result = companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
            ex.message == "data is malformed"
        
        and:
            1 * companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            1 * companyFunctionsRepository.save(_ as CompanyFunctions) >> { throw new DataIntegrityViolationException("") }
    }

}