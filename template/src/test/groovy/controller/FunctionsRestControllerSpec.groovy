package controller

import pot.insurance.manager.dto.CompanyFunctionsDTO
import pot.insurance.manager.service.FunctionsService
import pot.insurance.manager.controller.FunctionsRestController

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.UUID

class FunctionsRestControllerSpec extends Specification {

    FunctionsService functionsService = Mock()

    @Subject
    FunctionsRestController functionsRestController = new FunctionsRestController(functionsService)

    @Unroll("test saveCompanyFunctions metod for successful save with defined #companyFunctionsDTO and #companyId")
    def "test saveCompanyFunctions metod for successful save"() {
        given: "CompanyFunctionsDTO object with valid data"
            def companyFunctionsDTO = new CompanyFunctionsDTO(
                null,
                true,
                false,
                true,
                true,
                false,
                true
            )
            
            def companyId = UUID.randomUUID()
        
        when: "is called method saveCompanyFunctions with companyFunctionsDTO"
            CompanyFunctionsDTO result = functionsRestController.saveCompanyFunctions(companyId, companyFunctionsDTO)
        
        then: "result is not null and equals companyFunctionsDTO"
            result != null
            result == companyFunctionsDTO
        
        and: "method for saving is called once"
            1 * functionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> companyFunctionsDTO
    }

    @Unroll("test saveCompanyFunctions metod for unsuccessful save with #companyFunctionsDTO")
    def "test saveCompanyFunctions metod for unsuccessful save should return null"() {
        given: "CompanyFunctionsDTO object with invalid data"
            def companyFunctionsDTO = null
            def companyId = UUID.randomUUID()
        
        when: "is called method saveCompanyFunctions with companyFunctionsDTO"
            CompanyFunctionsDTO response = functionsRestController.saveCompanyFunctions(companyId, companyFunctionsDTO)
        
        then: "response is null"
            response == null
        
        and: "save method is called once"
            1 * functionsService.saveCompanyFunctions(companyId, companyFunctionsDTO) >> null
    }
}
