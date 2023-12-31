package service

import org.springframework.dao.DataIntegrityViolationException

import pot.insurance.manager.dto.UserFunctionsDTO
import pot.insurance.manager.entity.CompanyFunctions
import pot.insurance.manager.entity.UserFunctions
import pot.insurance.manager.entity.User
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.repository.UserRepository
import pot.insurance.manager.mapper.UserFunctionsMapper
import pot.insurance.manager.repository.CompanyFunctionsRepository
import pot.insurance.manager.repository.UserFunctionsRepository
import pot.insurance.manager.service.UserFunctionsService
import pot.insurance.manager.service.UserFunctionsServiceImpl
import pot.insurance.manager.type.DataValidation

import spock.lang.Shared
import spock.lang.Unroll
import spock.lang.Specification

import java.util.UUID

class UserFunctionsServiceImplSpec extends Specification {

    

    UserFunctionsMapper userFunctionsMapper = UserFunctionsMapper.INSTANCE

    UserRepository userRepository = Mock()
    CompanyFunctionsRepository companyFunctionsRepository = Mock()
    UserFunctionsRepository userFunctionsRepository = Mock()

    @Shared
    UserFunctionsService userFunctionsService

    def setup() {
        userFunctionsService = new UserFunctionsServiceImpl(userFunctionsRepository, userRepository, companyFunctionsRepository)
    }

    @Unroll
    def "test saveUserFunctions method to return the saved DTO with provided values without throwing exception and all company functions enabled"() {
        given:
            def companyId = UUID.randomUUID()
            def userId = UUID.randomUUID()
            def userFunctionsDTO = new UserFunctionsDTO(
                userId: userId,
                companyId: companyId,
                companyManager: true,
                consumer: false,
                companyClaimManager: false,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            )

            def companyFunctions = new CompanyFunctions(
                id: companyId,
                companyManager: true,
                consumer: true,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: true,
                companyReportManager: true
            )
        
        when: "saveUserFunctions method is called and company functions are set true then user functions are saved true or false"
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            companyFunctionsRepository.findById(companyId) >> Optional.of(companyFunctions)
            userFunctionsRepository.save(_ as UserFunctions) >> userFunctionsMapper.userFunctionsDTOToEntity(userFunctionsDTO)
            def result = userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO)

        then:
            result != null
            result == userFunctionsDTO
            notThrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            1 * companyFunctionsRepository.findById(companyId) >> Optional.of(companyFunctions)
            1 * userFunctionsRepository.save(_) >> userFunctionsMapper.userFunctionsDTOToEntity(userFunctionsDTO)

    }

    @Unroll
    def "test saveUserFunctions method to return the DTO but with different values for disabled company functions without throwing exception"() {
        given:
            def companyId = UUID.randomUUID()
            def userId = UUID.randomUUID()
            def userFunctionsDTO = new UserFunctionsDTO(
                userId: userId,
                companyId: companyId,
                companyManager: true,
                consumer: true,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: true,
                companyReportManager: true
            )

            def companyFunctions = new CompanyFunctions(
                id: companyId,
                companyManager: false,
                consumer: false,
                companyClaimManager: false,
                consumerClaimManager: false,
                companySettingManager: true,
                companyReportManager: true
            )
        
        when: "saveUserFunctions method is called and company functions are set false then user functions are saved false"
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            companyFunctionsRepository.findById(companyId) >> Optional.of(companyFunctions)
            userFunctionsRepository.save(_ as UserFunctions) >> userFunctionsMapper.userFunctionsDTOToEntity(userFunctionsDTO)
            UserFunctionsDTO result = userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO)

        then:
            result != null
            result != userFunctionsDTO
            notThrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            1 * companyFunctionsRepository.findById(companyId) >> Optional.of(companyFunctions)
            1 * userFunctionsRepository.save(_) >> userFunctionsMapper.userFunctionsDTOToEntity(userFunctionsDTO)
    }

    @Unroll
    def "expect saveUserFunctions method to throw an exception when user is not found"() {
        given:
            def companyId = UUID.randomUUID()
            def userId = UUID.randomUUID()
            def userFunctionsDTO = new UserFunctionsDTO()
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.empty()
            def result = userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO)
        
        then:
            result == null
            thrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.empty()
            0 * companyFunctionsRepository.findById(companyId) >> Optional.of(new CompanyFunctions())
            0 * userFunctionsRepository.save(_) >> userFunctionsMapper.userFunctionsDTOToEntity(userFunctionsDTO)
    }

    @Unroll
    def "expect saveUserFunctions method to throw an exception when company functions are not set"() {
        given:
            def companyId = UUID.randomUUID()
            def userId = UUID.randomUUID()
            def userFunctionsDTO = new UserFunctionsDTO()
        
        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            companyFunctionsRepository.findById(companyId) >> Optional.empty()
            def result = userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO)
        
        then:
            result == null
            thrown(DataValidationException)
        
        and:
            1 * userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            1 * companyFunctionsRepository.findById(companyId) >> Optional.empty()
            0 * userFunctionsRepository.save(_) >> userFunctionsMapper.userFunctionsDTOToEntity(userFunctionsDTO)
    }

    @Unroll
    def "expect saveUserFunctions method to throw an exception when a DataIntegrityViolationException occurs"() {
        given:
            def companyId = UUID.randomUUID()
            def userId = UUID.randomUUID()
            def userFunctionsDTO = new UserFunctionsDTO()
            def companyFunctions = new CompanyFunctions()

        when:
            userRepository.findByIdAndDeletionStatusFalse(userId) >> Optional.of(new User())
            companyFunctionsRepository.findById(companyId) >> Optional.of(companyFunctions)
            userFunctionsRepository.save(_) >> { throw new DataIntegrityViolationException("") }
            def result = userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO)
        
        then:
            result == null
            thrown(DataValidationException)
    }

}

