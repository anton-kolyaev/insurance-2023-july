package integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup


import org.springframework.http.MediaType
import org.springframework.http.HttpStatus
import java.util.UUID
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import pot.insurance.manager.controller.CompanyFunctionsRestController
import pot.insurance.manager.dto.CompanyFunctionsDTO
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.type.DataValidation
import pot.insurance.manager.service.CompanyFunctionsService

class CompanyFunctionsIntegrationSpec extends Specification {

    ObjectMapper objectMapper = new ObjectMapper()
    MockMvc mockMvc

    CompanyFunctionsService companyFunctionsService = Spy(CompanyFunctionsService)

    @Subject
    CompanyFunctionsRestController companyFunctionsRestController

    def setup() {
        companyFunctionsRestController = new CompanyFunctionsRestController(companyFunctionsService)
        mockMvc = MockMvcBuilders.standaloneSetup(companyFunctionsRestController).build()
    }

    @Unroll
    def "test saveCompanyFunctions success shoud return 200 http status"() {
        given:
            UUID companyId = UUID.randomUUID()
            CompanyFunctionsDTO companyFunctionsDTO = new CompanyFunctionsDTO(
                id: null,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            )
        
        when:
            companyFunctionsRestController.saveCompanyFunctions(companyId, companyFunctionsDTO) >> companyFunctionsDTO
        
        and:
            def response = mockMvc.perform(post("/v1/companies/{companyId}/functions", companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(companyFunctionsDTO)))
        
        then:
            response.andExpect(status().is(HttpStatus.OK.value()))
        
        and:
            1 * companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> companyFunctionsDTO
    }
}
