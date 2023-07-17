package controller

import pot.insurance.manager.dto.UserFunctionsDTO
import pot.insurance.manager.service.UserFunctionsService
import pot.insurance.manager.controller.UserFunctionsRestController
import pot.insurance.manager.exception.DataValidationException

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.util.UUID

class UserFunctionsRestControllerSpec extends Specification {

    UserFunctionsService userFunctionsService = Mock()

    @Subject
    UserFunctionsRestController userFunctionsRestController = new UserFunctionsRestController(userFunctionsService)

    @Unroll("test saveUserFunctions metod for successful save with defined #userFunctionsDTO, #companyId and #userId")
    def "test saveUserFunctions metod for successful save"() {
        given: "UserFunctionsDTO object with valid data"
            def companyId = UUID.randomUUID()
            def userId = UUID.randomUUID()
            def userFunctionsDTO = new UserFunctionsDTO(
                companyId: companyId,
                userId: userId,
                companyManager: true,
                consumer: false,
                companyClaimManager: true,
                consumerClaimManager: true,
                companySettingManager: false,
                companyReportManager: true
            )
        
        when: "is called method saveUserFunctions with userFunctionsDTO"
            UserFunctionsDTO result = userFunctionsRestController.saveUserFunctions(companyId, userId, userFunctionsDTO)
        
        then: "result is not null and equals userFunctionsDTO"
            result != null
            result == userFunctionsDTO
            notThrown(DataValidationException)
        
        and: "method for saving is called once"
            1 * userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> userFunctionsDTO
    }

    @Unroll("test saveUserFunctions metod for unsuccessful save with #userFunctionsDTO, #companyId and #userId")
    def "test saveUserFunctions metod for unsuccessful save should return null"() {
        given: "UserFunctionsDTO object with invalid data"
            def companyId = UUID.randomUUID()
            def userId = UUID.randomUUID()
            def userFunctionsDTO = null
        
        when: "is called method saveUserFunctions with userFunctionsDTO"
            UserFunctionsDTO response = userFunctionsRestController.saveUserFunctions(companyId, userId, userFunctionsDTO)
        
        then: "response is null"
            response == null
        
        and: "save method is called once"
            1 * userFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO) >> null
    }

}