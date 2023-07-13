package service

import org.springframework.dao.DataIntegrityViolationException

import pot.insurance.manager.dto.CompanyFunctionsDTO
import pot.insurance.manager.entity.CompanyFunctions
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.type.DataValidation
import pot.insurance.manager.mapper.FunctionsMapper
import pot.insurance.manager.repository.CompanyFunctionsRepository
import pot.insurance.manager.repository.CompanyRepository
import pot.insurance.manager.service.FunctionsServiceImpl
import pot.insurance.manager.entity.Company

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Shared
import spock.lang.Unroll

import java.util.Optional
import java.util.UUID
import java.sql.Date

class FunctionsServiceSpec extends Specification{

    
    CompanyRepository companyRepository = Mock()
    CompanyFunctionsRepository companyFunctionsRepository = Mock()
    FunctionsMapper functionsMapper = FunctionsMapper.INSTANCE

    @Shared
    FunctionsServiceImpl functionsService


    def setup() {
        functionsService = new FunctionsServiceImpl(companyRepository, companyFunctionsRepository)
    }

    @Unroll
    def "test saveCompanyFunctions - successful case"() {
        given:
            def companyId = UUID.randomUUID()
            def companyFunctionsDTO = new CompanyFunctionsDTO(
                companyId,
                true,
                false,
                false,
                false,
                true,
                false
            )
        
        when:
            companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            companyFunctionsRepository.save(_ as CompanyFunctions) >> functionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO)
            def result = functionsService.saveCompanyFunctions(companyId, companyFunctionsDTO)

        then:
            result != null
            result == companyFunctionsDTO
            notThrown(DataValidationException)
        and:
            1 * companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            1 * companyFunctionsRepository.save(_ as CompanyFunctions) >> functionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO)
    }


    @Unroll
    def "test saveCompanyFunctions shoul throw exception when company is not found"() {
        given:
            UUID companyId = UUID.randomUUID()
            def companyFunctionsDTO = new CompanyFunctionsDTO(
                companyId,
                true,
                false,
                false,
                false,
                true,
                false
            )

        when:
            companyRepository.findByIdAndDeletionStatusFalse(_) >> Optional.empty()
            companyFunctionsRepository.save(_ as CompanyFunctions) >> functionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO)
            def result = functionsService.saveCompanyFunctions(companyId, companyFunctionsDTO)

        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
        
        and:
            1 * companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.empty()
            0 * companyFunctionsRepository.save(_ as CompanyFunctions) >> functionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO)
    
    }

    @Unroll
    def "test saveCompanyFunctions shoul throw exception when data is malformed"() {
        given:
            UUID companyId = UUID.randomUUID()
            CompanyFunctionsDTO companyFunctionsDTO = new CompanyFunctionsDTO()
        
        when:
            companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            companyFunctionsRepository.save(_ as CompanyFunctions) >> { throw new DataIntegrityViolationException("") }
            def result = functionsService.saveCompanyFunctions(companyId, companyFunctionsDTO)
        
        then:
            result == null
            DataValidationException ex = thrown(DataValidationException)
            ex.message == "data is malformed"
        
        and:
            1 * companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            1 * companyFunctionsRepository.save(_ as CompanyFunctions) >> { throw new DataIntegrityViolationException("") }
    }

}