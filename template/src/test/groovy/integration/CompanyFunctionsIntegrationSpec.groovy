package integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


import org.springframework.http.MediaType
import org.springframework.http.HttpStatus
import java.util.UUID
import java.util.Optional

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

import pot.insurance.manager.mapper.CompanyFunctionsMapper
import pot.insurance.manager.controller.RestExceptionHandler
import pot.insurance.manager.mapper.StatusMapper



import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@Import(RestExceptionHandler.class)
@WebMvcTest(CompanyFunctionsRestController.class)
class CompanyFunctionsIntegrationSpec extends Specification {

    ObjectMapper objectMapper = new ObjectMapper()
 
    MockMvc mockMvc
    CompanyRepository companyRepository = Mock(CompanyRepository)
    CompanyFunctionsRepository companyFunctionsRepository = Mock(CompanyFunctionsRepository)


    CompanyFunctionsService companyFunctionsService

    @Subject
    CompanyFunctionsRestController companyFunctionsRestController

    def setup() {
        companyFunctionsService = Spy(new CompanyFunctionsServiceImpl(companyRepository, companyFunctionsRepository))
        companyFunctionsRestController = new CompanyFunctionsRestController(companyFunctionsService)
        mockMvc = MockMvcBuilders.standaloneSetup(companyFunctionsRestController).build()
    }

    @Unroll
    def "test saveCompanyFunctions success shoud return 200 http status"() {
        given:
            UUID companyId = UUID.randomUUID()
            CompanyFunctionsDTO companyFunctionsDTO = Spy(new CompanyFunctionsDTO(
                id: companyId,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            ))
        
        when:
            def response = mockMvc.perform(post("/v1/companies/{companyId}/functions", companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(companyFunctionsDTO)))
        
        then:
            response.andExpect(status().is(HttpStatus.OK.value()))
        
        and:
            1 * companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> companyFunctionsDTO
    }

    @Unroll
    def "test saveCompanyFunctions should return 404 http status when route is not found"() {
        given:
            UUID companyId = UUID.randomUUID()
            CompanyFunctionsDTO companyFunctionsDTO = Spy( new CompanyFunctionsDTO(
                id: companyId,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            ))
        
        when:
        
            def response = mockMvc.perform(post("/v1/companies/{companyId}/funct", companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(companyFunctionsDTO)))
        
        then:
            response.andExpect(status().is(HttpStatus.NOT_FOUND.value()))
            0 * companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> companyFunctionsDTO
        
    }

    @Unroll
    def "test saveCompanyFunctions should return 404 HTTP status when companyId is not found"() {
        given:
            UUID companyId = UUID.randomUUID()
            CompanyFunctionsDTO companyFunctionsDTO = Spy(new CompanyFunctionsDTO(
                id: null,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            ))

            companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.COMPANY_NOT_FOUND)}

            def response
        
        when:
            try{
                response = mockMvc.perform(post("/v1/companies/{companyId}/functions", companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(companyFunctionsDTO)))
            }catch (ServletException ex) {
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
            1 * companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.COMPANY_NOT_FOUND)}
    }

    @Unroll
    def "test saveCompanyFunctions should return 400 HTTP status on data integrity violation"() {
        given:
            def response
            UUID companyId = UUID.randomUUID()
            CompanyFunctionsDTO companyFunctionsDTO = Spy(new CompanyFunctionsDTO(
                id: null,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            ))
            companyRepository.findByIdAndDeletionStatusFalse(companyId) >> Optional.of(new Company())
            companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.MALFORMED_DATA)}
        
        when:
            try {
                response = mockMvc.perform(post("/v1/companies/{companyId}/functions", companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(companyFunctionsDTO)))
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
            1 * companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> {throw new DataValidationException(DataValidation.Status.MALFORMED_DATA)}
    }
}
