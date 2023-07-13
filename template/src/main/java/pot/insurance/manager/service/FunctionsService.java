package pot.insurance.manager.service;

import java.util.UUID;

import pot.insurance.manager.dto.CompanyFunctionsDTO;


public interface FunctionsService {
    
    CompanyFunctionsDTO saveCompanyFunctions(UUID id, CompanyFunctionsDTO companyFunctionsDTO);
    
}
