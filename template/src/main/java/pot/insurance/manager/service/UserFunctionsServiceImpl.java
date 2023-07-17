package pot.insurance.manager.service;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import pot.insurance.manager.dto.UserFunctionsDTO;
import pot.insurance.manager.entity.CompanyFunctions;
import pot.insurance.manager.entity.UserFunctions;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.repository.UserRepository;
import pot.insurance.manager.mapper.UserFunctionsMapper;
import pot.insurance.manager.repository.CompanyFunctionsRepository;
import pot.insurance.manager.repository.UserFunctionsRepository;
import pot.insurance.manager.type.DataValidation;

@Service
@RequiredArgsConstructor
public class UserFunctionsServiceImpl implements UserFunctionsService {

    private final UserFunctionsRepository userFunctionsRepository;
    private final UserRepository userRepository;
    private final CompanyFunctionsRepository companyFunctionsRepository;
    private static final UserFunctionsMapper userFunctionsMapper = UserFunctionsMapper.INSTANCE;

    @Override
    public UserFunctionsDTO saveUserFunctions(UUID companyId, UUID userId, UserFunctionsDTO userFunctionsDTO) {
        try {
            userRepository.findByIdAndDeletionStatusFalse(userId)
                .orElseThrow(() -> new DataValidationException(DataValidation.Status.USER_NOT_FOUND));
            CompanyFunctions companyFunctions = companyFunctionsRepository.findById(companyId)
                .orElseThrow(() -> new DataValidationException(DataValidation.Status.COMPANY_FUNCTIONS_NOT_SETTED));

            userFunctionsDTO.setCompanyManager(companyFunctions.isCompanyManager() && userFunctionsDTO.isCompanyManager());
            userFunctionsDTO.setCompanyClaimManager(companyFunctions.isCompanyClaimManager() && userFunctionsDTO.isCompanyClaimManager());
            userFunctionsDTO.setCompanyReportManager(companyFunctions.isCompanyReportManager() && userFunctionsDTO.isCompanyReportManager());
            userFunctionsDTO.setCompanySettingManager(companyFunctions.isCompanySettingManager() && userFunctionsDTO.isCompanySettingManager());
            userFunctionsDTO.setConsumer(companyFunctions.isConsumer() && userFunctionsDTO.isConsumer());
            userFunctionsDTO.setConsumerClaimManager(companyFunctions.isConsumerClaimManager() && userFunctionsDTO.isConsumerClaimManager());
                
            UserFunctions userFunctions = userFunctionsMapper.userFunctionsDTOToEntity(userFunctionsDTO);
            userFunctions.setUserId(userId);
            userFunctions.setCompanyId(companyId);

            return userFunctionsMapper.userFunctionsEntityToDTO(userFunctionsRepository.save(userFunctions));
        } catch (DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }

    
    
}
