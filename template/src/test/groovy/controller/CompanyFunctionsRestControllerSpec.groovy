package controller

import pot.insurance.manager.dto.CompanyFunctionsDTO
import pot.insurance.manager.service.CompanyFunctionsService
import pot.insurance.manager.controller.CompanyFunctionsRestController

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class CompanyFunctionsRestControllerSpec extends Specification {

    CompanyFunctionsService service = Mock()

    @Subject
    CompanyFunctionsRestController controller = new CompanyFunctionsRestController(service)

    @Unroll
    def "test saveCompanyFunctions metod for successful save"() {
        given: "CompanyFunctionsDTO object with valid data"
        def dto = new CompanyFunctionsDTO(
            id: null,
            companyManager: true,
            consumer: false,
            companyClaimManager: true,
            consumerClaimManager: true,
            companySettingManager: false,
            companyReportManager: true
        )

        def companyId = UUID.randomUUID()
        
        when: "is called method saveCompanyFunctions with companyFunctionsDTO"
        CompanyFunctionsDTO result = controller.saveCompanyFunctions(companyId, dto)
        
        then: "result is not null and equals companyFunctionsDTO"
        result != null
        result == dto
        
        and: "method for saving is called once"
        1 * service.saveCompanyFunctions(companyId, dto) >> dto
    }

    @Unroll
    def "test saveCompanyFunctions metod for unsuccessful save should return null"() {
        given: "CompanyFunctionsDTO object with invalid data"
        def dto = null
        def id = UUID.randomUUID()
        
        when: "is called method saveCompanyFunctions with companyFunctionsDTO"
        CompanyFunctionsDTO response = controller.saveCompanyFunctions(id, dto)
        
        then: "response is null"
        response == null
        
        and: "save method is called once"
        1 * service.saveCompanyFunctions(id, dto) >> null
    }
}
