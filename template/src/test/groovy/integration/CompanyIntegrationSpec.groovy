package integration

import base.TestableTrait
import jakarta.servlet.ServletException
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pot.insurance.manager.controller.CompanyRestController
import pot.insurance.manager.dto.CompanyDTO
import pot.insurance.manager.entity.Company
import pot.insurance.manager.exception.DataValidationException
import pot.insurance.manager.mapper.StatusMapper
import pot.insurance.manager.repository.CompanyRepository
import pot.insurance.manager.service.CompanyService
import spock.lang.Specification
import com.fasterxml.jackson.databind.ObjectMapper
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@WebMvcTest(CompanyRestController.class)
class CompanyIntegrationSpec extends Specification implements TestableTrait {
    CompanyRepository companyRepository = Mock()
    CompanyService companyService = Spy(constructorArgs: [companyRepository])
    private static ObjectMapper mapper = new ObjectMapper();
    protected MockMvc mockMvc

    def setup() {
        mockMvc = standaloneSetup(new CompanyRestController(companyService)).build()
    }

    def "expect corresponding status code when performing POST request to save new company"() {
        given:
        CompanyDTO companyDTO = new CompanyDTO(id, name, code, email, site, deletionStatus)
        String json = mapper.writeValueAsString(companyDTO)
        companyRepository.findById(id) >> optional

        when:
        def result
        try {
            result = mockMvc.perform(post('/v1/companies')
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andReturn()
                    .response
                    .getStatus()
        } catch (ServletException ex) {
            Throwable rootCause = ((ServletException) ex).getRootCause()
            if(rootCause instanceof DataValidationException) {
                result = StatusMapper.toHttp(rootCause.getStatus().getCategory()).value()
            }
        }

        then:
        assertReceivedDataAreAsExpected(result, status)

        where:
            id            | code |        name       |      site       |     email         | deletionStatus | status
        UUID.randomUUID() | "US" | "Example company" | "example.com"   | "email@gmail.com" |       false    | 201
        UUID.randomUUID() | "US" | "Example company" |     "A"         |     "a"           |       false    | 409
        UUID.randomUUID() | "US" |       null        |     "A"         |     "a"           |       false    | 400
        UUID.randomUUID() | "BY" |   "New Health"    |     "A"         |     "a"           |       true     | 409
        UUID.randomUUID() | "PL" |   "Save deleted"  | "deleted.com"   | "test@test.mail"  |       true     | 201
        
        and:

        optional << [
                Optional.empty(),
                Optional.of(new Company(UUID.randomUUID(), "US", "Example company", "A", "a", false )),
                Optional.empty(),
                Optional.of(new Company(UUID.randomUUID(), "PL", "Save deleted", "deleted.com", "test@test.mail", true )),
                Optional.empty()
        ]
    }

    def "expect 200 status code when performing GET request to retrieve all existing companies"() {
        given:
        companyRepository.findAll() >> list

        when:
        def result = mockMvc.perform(get('/v1/companies')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .response
                .getStatus()

        then:
        assertReceivedDataAreAsExpected(result, 200)

        where:
        list << [
                List.of(),
                List.of(new Company(UUID.randomUUID(), "US", "Example company", "A", "a", false))
        ]
    }

    def "expect corresponding status code when performing GET request to retrieve company by ID"() {
        given:
        companyRepository.findByIdAndDeletionStatusFalse(id) >> optional

        when:
        def result
        try {
            result = mockMvc.perform(get('/v1/companies/{id}', id.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .response
                    .getStatus()
        } catch (ServletException ex) {
            Throwable rootCause = ex.getRootCause()
            if(rootCause instanceof DataValidationException) {
                result = StatusMapper.toHttp(rootCause.getStatus().getCategory()).value()
            }
        }

        then:
        assertReceivedDataAreAsExpected(result, status)

        where:
        id << [
                UUID.fromString("2e3949b1-29bf-46a3-b7c9-9d82a576583e"),
                UUID.fromString("9ab0d8c7-08d4-4b5c-8d34-5876a905f3b7")
        ]
        optional << [
                Optional.of(new Company(UUID.fromString("2e3949b1-29bf-46a3-b7c9-9d82a576583e"), "Company", "US", "email@gmail.com", "site.com", false)),
                Optional.empty()
        ]
        status << [
                200,
                404
        ]
    }
}