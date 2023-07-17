package pot.insurance.manager.service;

import java.lang.reflect.Field;
import java.util.Arrays;
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
            
            UserFunctionsDTO modifiedUserFunctionsDTO = modifyUserFunctionsDTO(userFunctionsDTO, companyFunctions);
            UserFunctions userFunctions = userFunctionsMapper.userFunctionsDTOToEntity(modifiedUserFunctionsDTO);
            userFunctions.setUserId(userId);
            userFunctions.setCompanyId(companyId);
            System.out.println(userFunctions);

            return userFunctionsMapper.userFunctionsEntityToDTO(userFunctionsRepository.save(userFunctions));
        } catch (DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }

    @Override
    public UserFunctionsDTO modifyUserFunctionsDTO(UserFunctionsDTO userFunctionsDTO, CompanyFunctions companyFunctions) {
        Arrays.stream(userFunctionsDTO.getClass().getDeclaredFields())
                .filter(field -> field.getType() == Boolean.TYPE)
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        Field companyField = companyFunctions.getClass().getDeclaredField(fieldName);
                        companyField.setAccessible(true);
                        boolean companyFunctionValue = (boolean) companyField.get(companyFunctions);
                        if (!companyFunctionValue) {
                            field.set(userFunctionsDTO, false);
                        }
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        if (e instanceof IllegalAccessException) {
                            throw new DataValidationException(DataValidation.Status.ILLEGAL_ACCEPT);
                        } else {
                            throw new DataValidationException(DataValidation.Status.COMPANY_FUNCTIONS_NOT_SETTED);
                        }
                    }
                });
            
        return userFunctionsDTO;
    }
    
    
}
