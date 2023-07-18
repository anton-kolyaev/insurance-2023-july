package pot.insurance.manager.service;

import java.util.UUID;

import pot.insurance.manager.dto.UserFunctionsDTO;

public interface UserFunctionsService {

    UserFunctionsDTO saveUserFunctions(UUID companyId, UUID userId, UserFunctionsDTO userFunctionsDTO);
    
}
