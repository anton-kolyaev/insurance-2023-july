package pot.insurance.manager.service;

import java.util.UUID;

import pot.insurance.manager.dto.CompanyFunctionsDTO;

public interface CompanyFunctionsService {
    
    CompanyFunctionsDTO saveCompanyFunctions(UUID id, CompanyFunctionsDTO companyFunctionsDTO);
    
}
