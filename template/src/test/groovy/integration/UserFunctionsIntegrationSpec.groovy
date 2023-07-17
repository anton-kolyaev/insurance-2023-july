package integration

import com.fasterxml.jackson.databind.ObjectMapper

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.http.MediaType
import org.springframework.http.HttpStatus
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import java.util.UUID
import java.util.Optional

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import jakarta.servlet.ServletException

import pot.insurance.manager.controller.UserFunctionsRestController
import pot.insurance.manager.dto.UserFunctionsDTO
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.type.DataValidation
import pot.insurance.manager.service.UserFunctionsService
import pot.insurance.manager.service.UserFunctionsServiceImpl
import pot.insurance.manager.repository.UserRepository
import pot.insurance.manager.repository.CompanyFunctionsRepository
import pot.insurance.manager.repository.UserFunctionsRepository
import pot.insurance.manager.entity.User
import pot.insurance.manager.entity.CompanyFunctions
import pot.insurance.manager.mapper.UserFunctionsMapper
import pot.insurance.manager.controller.RestExceptionHandler
import pot.insurance.manager.mapper.StatusMapper


@WebMvcTest(UserFunctionsRestController.class)
@Import(RestExceptionHandler.class)
class UserFunctionsIntegrationSpec extends Specification {

    ObjectMapper objectMapper = new ObjectMapper()

    MockMvc mockMvc
    UserRepository userRepository = Mock(UserRepository)
    CompanyFunctionsRepository  companyFunctionsRepository = Mock(CompanyFunctionsRepository)
    UserFunctionsRepository userFunctionsRepository = Mock(UserFunctionsRepository)

    UserFunctionsService userFunctionsService

    @Subject
    UserFunctionsRestController userFunctionsRestController

    def setup() {
        userFunctionsService = Spy(new UserFunctionsServiceImpl(userFunctionsRepository, userRepository, companyFunctionsRepository))
        userFunctionsRestController = new UserFunctionsRestController(userFunctionsService)
        mockMvc = MockMvcBuilders.standaloneSetup(userFunctionsRestController).build()
    }

    @Unroll
    def "test saveUserFunctions should return 200 HTTP status"() {
        given:
            UUID companyId = UUID.randomUUID()
            UUID userId = UUID.randomUUID()
            UserFunctionsDTO userFunctionsDTO = new UserFunctionsDTO(
                userId: userId,
                companyId: companyId,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            )
        
        when:
            def response = mockMvc.perform(post("/v1/companies/{companyId}/users/{userId}/functions", companyId, userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userFunctionsDTO)))
        
        then:
            response.andExpect(status().is(HttpStatus.OK.value()))
        
        and:
            1 * userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> userFunctionsDTO
    }

    @Unroll
    def "test saveUserFunctions should return 404 HTTP status when company or user is not found"() {
        given:
            UUID companyId = UUID.randomUUID()
            UUID userId = UUID.randomUUID()
            UserFunctionsDTO userFunctionsDTO = new UserFunctionsDTO(
                    userId: null,
                    companyId: null,
                    companyManager: true,
                    consumer: false,
                    companyClaimManager: true,
                    consumerClaimManager: true,
                    companySettingManager: false,
                    companyReportManager: true
            )

            userRepository.findById(userId) >> Optional.empty()
            userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {
                throw new DataValidationException(DataValidation.Status.USER_NOT_FOUND)}
            def response
        
        when:
            try{
                response = mockMvc.perform(post("/v1/companies/{companyId}/users/{userId}/functions", companyId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFunctionsDTO)))
            } catch (ServletException ex) {
                Throwable rootCause = ((ServletException) ex).getRootCause()
                if (rootCause instanceof DataValidationException) {
                    DataValidationException dataValidationException = (DataValidationException) rootCause
                    HttpStatus httpStatus = StatusMapper.toHttp(dataValidationException.getStatus().getCategory())
                    response = httpStatus
                }
            }
        
        then:
            response == HttpStatus.NOT_FOUND
        
        and:
            1 * userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.USER_NOT_FOUND)}
    }

    @Unroll
    def "test saveUserFunctions should return 404 HTTP status when companyfunctions is not found"() {
        given:
            UUID companyId = UUID.randomUUID()
            UUID userId = UUID.randomUUID()
            UserFunctionsDTO userFunctionsDTO = new UserFunctionsDTO(
                    userId: null,
                    companyId: null,
                    companyManager: true,
                    consumer: false,
                    companyClaimManager: true,
                    consumerClaimManager: true,
                    companySettingManager: false,
                    companyReportManager: true
            )

            userRepository.findById(userId) >> Optional.of(new User())
            companyFunctionsRepository.findById(companyId) >> Optional.empty()
            userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {
                throw new DataValidationException(DataValidation.Status.COMPANY_FUNCTIONS_NOT_SETTED)}
            def response
        
        when:
            try{
                response = mockMvc.perform(post("/v1/companies/{companyId}/users/{userId}/functions", companyId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFunctionsDTO)))
            } catch (ServletException ex) {
                Throwable rootCause = ((ServletException) ex).getRootCause()
                if (rootCause instanceof DataValidationException) {
                    DataValidationException dataValidationException = (DataValidationException) rootCause
                    HttpStatus httpStatus = StatusMapper.toHttp(dataValidationException.getStatus().getCategory())
                    response = httpStatus
                }
            }
        
        then:
            response == HttpStatus.NOT_FOUND
        
        and:
            1 * userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.COMPANY_FUNCTIONS_NOT_SETTED)}
    }

    @Unroll
    def "test saveUserFunctions should return 404 HTTP status when companyfunctions is not setted"() {
        given:
            UUID companyId = UUID.randomUUID()
            UUID userId = UUID.randomUUID()
            UserFunctionsDTO userFunctionsDTO = new UserFunctionsDTO(
                    userId: null,
                    companyId: null,
                    companyManager: true,
                    consumer: false,
                    companyClaimManager: true,
                    consumerClaimManager: true,
                    companySettingManager: false,
                    companyReportManager: true
            )

            userRepository.findById(userId) >> Optional.of(new User())
            companyFunctionsRepository.findById(companyId) >> Optional.of(new CompanyFunctions())
            userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {
                throw new DataValidationException(DataValidation.Status.COMPANY_FUNCTIONS_NOT_SETTED)}
            def response
        
        when:
            try{
                response = mockMvc.perform(post("/v1/companies/{companyId}/users/{userId}/functions", companyId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFunctionsDTO)))
            } catch (ServletException ex) {
                Throwable rootCause = ((ServletException) ex).getRootCause()
                if (rootCause instanceof DataValidationException) {
                    DataValidationException dataValidationException = (DataValidationException) rootCause
                    HttpStatus httpStatus = StatusMapper.toHttp(dataValidationException.getStatus().getCategory())
                    response = httpStatus
                }
            }
        
        then:
            response == HttpStatus.NOT_FOUND
        
        and:
            1 * userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.COMPANY_FUNCTIONS_NOT_SETTED)}
    }

    @Unroll
    def "test saveUserFunctions should return 400 HTTP status when throw illigal accept exception"() {
        given:
            UUID companyId = UUID.randomUUID()
            UUID userId = UUID.randomUUID()
            UserFunctionsDTO userFunctionsDTO = new UserFunctionsDTO(
                    userId: null,
                    companyId: null,
                    companyManager: true,
                    consumer: false,
                    companyClaimManager: true,
                    consumerClaimManager: true,
                    companySettingManager: false,
                    companyReportManager: true
            )

            userRepository.findById(userId) >> Optional.of(new User())
            companyFunctionsRepository.findById(companyId) >> Optional.of(new CompanyFunctions())
            userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {
                throw new DataValidationException(DataValidation.Status.ILLEGAL_ACCEPT)}
            def response
        
        when:
            try{
                response = mockMvc.perform(post("/v1/companies/{companyId}/users/{userId}/functions", companyId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFunctionsDTO)))
            } catch (ServletException ex) {
                Throwable rootCause = ((ServletException) ex).getRootCause()
                if (rootCause instanceof DataValidationException) {
                    DataValidationException dataValidationException = (DataValidationException) rootCause
                    HttpStatus httpStatus = StatusMapper.toHttp(dataValidationException.getStatus().getCategory())
                    response = httpStatus
                }
            }
        
        then:
            response == HttpStatus.BAD_REQUEST
        
        and:
            1 * userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.ILLEGAL_ACCEPT)}
    }

    @Unroll
    def "test saveUserFunctions should return 400 HTTP status when data integrity violation"() {
        given:
            UUID companyId = UUID.randomUUID()
            UUID userId = UUID.randomUUID()
            UserFunctionsDTO userFunctionsDTO = new UserFunctionsDTO(
                    userId: null,
                    companyId: null,
                    companyManager: true,
                    consumer: false,
                    companyClaimManager: true,
                    consumerClaimManager: true,
                    companySettingManager: false,
                    companyReportManager: true
            )

            userRepository.findById(userId) >> Optional.of(new User())
            companyFunctionsRepository.findById(companyId) >> Optional.of(new CompanyFunctions())
            userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {
                throw new DataValidationException(DataValidation.Status.MALFORMED_DATA)}
            def response
        
        when:
            try{
                response = mockMvc.perform(post("/v1/companies/{companyId}/users/{userId}/functions", companyId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFunctionsDTO)))
            } catch (ServletException ex) {
                Throwable rootCause = ((ServletException) ex).getRootCause()
                if (rootCause instanceof DataValidationException) {
                    DataValidationException dataValidationException = (DataValidationException) rootCause
                    HttpStatus httpStatus = StatusMapper.toHttp(dataValidationException.getStatus().getCategory())
                    response = httpStatus
                }
            }
        
        then:
            response == HttpStatus.BAD_REQUEST
        
        and:
            1 * userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.MALFORMED_DATA)}
    }
}