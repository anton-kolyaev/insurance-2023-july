package pot.insurance.manager.service;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pot.insurance.manager.dto.CompanyFunctionsDTO;
import pot.insurance.manager.entity.CompanyFunctions;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.type.DataValidation;
import pot.insurance.manager.mapper.CompanyFunctionsMapper;
import pot.insurance.manager.repository.CompanyFunctionsRepository;
import pot.insurance.manager.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CompanyFunctionsServiceImpl implements CompanyFunctionsService {

    private final CompanyRepository companyRepository;
    private final CompanyFunctionsRepository companyFunctionsRepository;
    private static final CompanyFunctionsMapper companyFunctionsMapper = CompanyFunctionsMapper.INSTANCE;

    @Override
    public CompanyFunctionsDTO saveCompanyFunctions(UUID id, CompanyFunctionsDTO companyFunctionsDTO) {
        try {
            companyRepository.findByIdAndDeletionStatus(id, false).orElseThrow(() ->
                new DataValidationException(DataValidation.Status.COMPANY_NOT_FOUND)
            );
            CompanyFunctions companyFunctions = companyFunctionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO);
            companyFunctions.setId(id);
            return companyFunctionsMapper.companyFunctionsEntityToDTO(companyFunctionsRepository.save(companyFunctions));
        } catch (DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }

    
    
}
