package pot.insurance.manager.service;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pot.insurance.manager.dto.UserAuthDTO;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.mapper.UserMapper;
import pot.insurance.manager.repository.UserRepository;
import pot.insurance.manager.type.DataValidation;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository repository;

    private final UserMapper mapper;

    private final UserAuthService service;

    @Override
    public UserDTO save(UserDTO dto) {
        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID());
        }
        if (this.repository.existsById(dto.getId())) {
            throw new DataValidationException(DataValidation.Status.USER_ID_EXISTS);
        }
        if (this.repository.existsBySsn(dto.getSsn())) {
            throw new DataValidationException(DataValidation.Status.USER_SSN_EXISTS);
        }
        UserAuthDTO authDTO = dto.getAuth();
        if (authDTO != null) {
            authDTO = this.service.save(authDTO);
            dto.setAuth(authDTO);
        }
        User entity = this.mapper.toEntity(dto);
        try {
            UserDTO finalDTO = this.mapper.toDTO(this.repository.save(entity));
            finalDTO.setAuth(authDTO);
            return finalDTO;
        } catch (DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }
    
    @Override
    public List<UserDTO> findAll(){
        return this.repository.findAllByDeletionStatus(false)
            .stream()
            .map(this.mapper::toDTO)
            .toList();
    }

    @Override
    public UserDTO find(UUID id){
        User user = this.repository.findByIdAndDeletionStatus(id, false).orElseThrow(() ->
            new DataValidationException(DataValidation.Status.USER_NOT_FOUND)
        );
        return this.mapper.toDTO(user);
}

    @Override
    public UserDTO update(UserDTO dto) {
        UUID id = dto.getId();
        if (id == null) {
            throw new DataValidationException(DataValidation.Status.USER_NOT_FOUND);
        }
        User entity = this.repository.findById(id).orElseThrow(() ->
            new DataValidationException(DataValidation.Status.USER_NOT_FOUND)
        );

        UserAuthDTO authDTO = dto.getAuth();
        UUID authId = entity.getAuthId();
        if (authDTO != null) {
            if (authId == null) {
                authDTO = this.service.save(authDTO);
                entity.setAuthId(authDTO.getId());
            } else {
                if (authDTO.getId() == null) {
                    authDTO.setId(authId);
                }
                authDTO = this.service.update(authDTO);
            }
        } else if (authId != null) {
            entity.setAuthId(null);
            this.service.delete(authId);
        }
        try {
            UserDTO finalDTO = this.mapper.toDTO(this.repository.save(entity));
            finalDTO.setAuth(authDTO);
            return finalDTO;
        } catch (DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }

    @Override
    public UserDTO delete(UUID id){
        User user = this.repository.findByIdAndDeletionStatus(id, false)
            .orElseThrow(() -> new DataValidationException(DataValidation.Status.USER_NOT_FOUND));
        user.setDeletionStatus(true);
        return this.mapper.toDTO(this.repository.save(user));
        
    }

}
