package integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


import org.springframework.http.MediaType
import org.springframework.http.HttpStatus

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import jakarta.servlet.ServletException

import pot.insurance.manager.controller.CompanyFunctionsRestController
import pot.insurance.manager.dto.CompanyFunctionsDTO
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.type.DataValidation
import pot.insurance.manager.service.CompanyFunctionsService
import pot.insurance.manager.service.CompanyFunctionsServiceImpl
import pot.insurance.manager.repository.CompanyRepository
import pot.insurance.manager.repository.CompanyFunctionsRepository
import pot.insurance.manager.entity.Company
import pot.insurance.manager.controller.RestExceptionHandler
import pot.insurance.manager.mapper.StatusMapper



import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@Import(RestExceptionHandler.class)
@WebMvcTest(CompanyFunctionsRestController.class)
class CompanyFunctionsIntegrationSpec extends Specification {

    @Shared
    ObjectMapper objectMapper = new ObjectMapper()

    CompanyRepository companyRepository = Mock(CompanyRepository)

    CompanyFunctionsService companyFunctionsService = Spy(new CompanyFunctionsServiceImpl(companyRepository, Mock(CompanyFunctionsRepository)))

    @Subject
    CompanyFunctionsRestController companyFunctionsRestController = new CompanyFunctionsRestController(companyFunctionsService)

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(companyFunctionsRestController).build()

    @Unroll
    def "test saveCompanyFunctions success should return 200 http status"() {
        given:
        UUID id = UUID.randomUUID()
        CompanyFunctionsDTO dto = Spy(new CompanyFunctionsDTO(
            id: id,
            companyManager: true,
            consumer: false,
            companyClaimManager: true,
            consumerClaimManager: true,
            companySettingManager: false,
            companyReportManager: true
        ))
        
        when:
        def response = mockMvc.perform(post("/v1/companies/{companyId}/functions", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        
        then:
        response.andExpect(status().is(HttpStatus.OK.value()))

        and:
        1 * companyFunctionsService.saveCompanyFunctions(id, dto) >> dto
    }

    @Unroll
    def "test saveCompanyFunctions should return 404 http status when route is not found"() {
        given:
        UUID id = UUID.randomUUID()
        CompanyFunctionsDTO dto = Spy( new CompanyFunctionsDTO(
            id: id,
            companyManager: true,
            consumer: false,
            companyClaimManager: true,
            consumerClaimManager: true,
            companySettingManager: false,
            companyReportManager: true
        ))
        
        when:
        def response = mockMvc.perform(post("/v1/companies/{companyId}/funct", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        
        then:
        response.andExpect(status().is(HttpStatus.NOT_FOUND.value()))
        0 * companyFunctionsService.saveCompanyFunctions(id, dto) >> dto
        
    }

    @Unroll
    def "test saveCompanyFunctions should return 404 HTTP status when companyId is not found"() {
        given:
        UUID id = UUID.randomUUID()
        CompanyFunctionsDTO dto = Spy(new CompanyFunctionsDTO(
            id: null,
            companyManager: true,
            consumer: false,
            companyClaimManager: true,
            consumerClaimManager: true,
            companySettingManager: false,
            companyReportManager: true
        ))


        when:
        companyFunctionsService.saveCompanyFunctions(id, dto) >> {throw new DataValidationException(DataValidation.Status.COMPANY_NOT_FOUND)}

        def response = null
        try {
            response = mockMvc.perform(post("/v1/companies/{companyId}/functions", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        } catch (ServletException exception) {
            Throwable rootCause = ((ServletException) exception).getRootCause()
            if (rootCause instanceof DataValidationException) {
                DataValidationException dataValidationException = (DataValidationException) rootCause
                HttpStatus httpStatus = StatusMapper.toHttp(dataValidationException.getStatus().getCategory())
                response = httpStatus
            }
        }
        then:
        response == HttpStatus.NOT_FOUND

        and:
        1 * companyFunctionsService.saveCompanyFunctions(id, dto) >> {throw new DataValidationException(DataValidation.Status.COMPANY_NOT_FOUND)}
    }

    @Unroll
    def "test saveCompanyFunctions should return 400 HTTP status on data integrity violation"() {
        given:
        UUID id = UUID.randomUUID()
        CompanyFunctionsDTO dto = Spy(new CompanyFunctionsDTO(
            id: null,
            companyManager: true,
            consumer: false,
            companyClaimManager: true,
            consumerClaimManager: true,
            companySettingManager: false,
            companyReportManager: true
        ))
        companyRepository.findByIdAndDeletionStatus(id, false) >> Optional.of(new Company())
        companyFunctionsService.saveCompanyFunctions(id, dto) >> { throw new DataValidationException(DataValidation.Status.MALFORMED_DATA) }

        when:
        def response = null
        try {
            response = mockMvc.perform(post("/v1/companies/{companyId}/functions", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        } catch (ServletException exception) {
            Throwable rootCause = ((ServletException) exception).getRootCause()
            if (rootCause instanceof DataValidationException) {
                DataValidationException dataValidationException = (DataValidationException) rootCause
                HttpStatus httpStatus = StatusMapper.toHttp(dataValidationException.getStatus().getCategory())
                response = httpStatus
            }
        }

        then:
        response == HttpStatus.BAD_REQUEST

        and:
        1 * companyFunctionsService.saveCompanyFunctions(id, dto) >> { throw new DataValidationException(DataValidation.Status.MALFORMED_DATA) }
    }
}
