package pot.insurance.manager.service;

import java.util.UUID;

import pot.insurance.manager.dto.UserFunctionsDTO;
import pot.insurance.manager.entity.CompanyFunctions;

public interface UserFunctionsService {

    UserFunctionsDTO saveUserFunctions(UUID companyId, UUID userId, UserFunctionsDTO userFunctionsDTO);
    
    UserFunctionsDTO modifyUserFunctionsDTO(UserFunctionsDTO userFunctionsDTO, CompanyFunctions companyFunctions);
}
