package integration

import base.TestableTrait
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pot.insurance.manager.controller.CompanyRestController
import pot.insurance.manager.dto.CompanyDTO
import pot.insurance.manager.service.CompanyService
import spock.lang.Specification
import com.fasterxml.jackson.databind.ObjectMapper
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CompanyIntegrationSpec extends Specification implements TestableTrait {

    private static ObjectMapper mapper = new ObjectMapper();
    def companyService = Mock(CompanyService)
    protected MockMvc mockMvc

    def setup() {
        mockMvc = standaloneSetup(new CompanyRestController(companyService)).build()
    }

    def "test for 'create company' functionality"() {
        given:
        CompanyDTO companyDTO = new CompanyDTO(id, name, code, email, site)
        companyService.saveCompany(companyDTO) >> companyDTO

        String json = mapper.writeValueAsString(companyDTO)

        when:
        def result = mockMvc.perform(post('/v1/companies')
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .response

        then:
        result.contentAsString.equals(json)
        result.getStatus() == 201

        where:
               id         | code |        name       |      site     |     email
        UUID.randomUUID() | "US" | "Example company" | "example.com" | "email@gmail.com"
        UUID.randomUUID() | "US" | "Example company" |     "A"       | "a"
    }

    def "test for 'display companies' functionality"() {
        when:
        def result = mockMvc.perform(get('/v1/companies')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .response

        then:
        assertReceivedDataAreAsExpected(result.getStatus(), 200)
    }

}
