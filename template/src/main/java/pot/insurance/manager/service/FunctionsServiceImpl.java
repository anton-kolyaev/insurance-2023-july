package pot.insurance.manager.service;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pot.insurance.manager.dto.CompanyFunctionsDTO;
import pot.insurance.manager.entity.CompanyFunctions;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.type.DataValidation;
import pot.insurance.manager.mapper.FunctionsMapper;
import pot.insurance.manager.repository.CompanyFunctionsRepository;
import pot.insurance.manager.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class FunctionsServiceImpl implements FunctionsService {

    private final CompanyRepository companyRepository;
    private final CompanyFunctionsRepository companyFunctionsRepository;
    private static final FunctionsMapper companyFunctionsMapper = FunctionsMapper.INSTANCE;

    @Override
    public CompanyFunctionsDTO saveCompanyFunctions(UUID id, CompanyFunctionsDTO companyFunctionsDTO) {
        try {
            companyRepository.findByIdAndDeletionStatusFalse(id)
                .orElseThrow(() -> new DataValidationException(DataValidation.Status.COMPANY_NOT_FOUND));
            CompanyFunctions companyFunctions = companyFunctionsMapper.companyFunctionsDTOToEntity(companyFunctionsDTO);
            companyFunctions.setId(id);
            return companyFunctionsMapper.companyFunctionsEntityToDTO(companyFunctionsRepository.save(companyFunctions));
        } catch (DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }

    
    
}
