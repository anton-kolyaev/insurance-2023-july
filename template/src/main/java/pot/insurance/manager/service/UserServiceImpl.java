package pot.insurance.manager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;
import pot.insurance.manager.repository.UserRepository;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.mapper.UserMapper;
import pot.insurance.manager.type.DataValidation;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDTO save(UserDTO userDTO) {
        User entity = mapper.toDTO(userDTO);
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        Optional<User> conflictEntity = this.repository.findById(entity.getId());
        if (conflictEntity.isPresent()) {
            throw new DataValidationException(DataValidation.Status.USER_ID_EXISTS);
        }
        Optional<User> ssnEntity = this.repository.findBySsn(entity.getSsn());
        if (ssnEntity.isPresent()) {
            throw new DataValidationException(DataValidation.Status.USER_SSN_EXISTS);
        }
        try {
            return mapper.toUser(repository.save(entity));
        } catch (DataIntegrityViolationException exception) {
            throw new DataValidationException(DataValidation.Status.USER_INVALID_DATA);
        }
    }
    
    @Override
    public List<UserDTO> findAll(){
        List<User> userEntities = repository.findAll();
        return userEntities.stream().map(mapper::toUser).toList();
    }

    @Override
    public UserDTO findById(UUID id){
        Optional<User> entity = this.repository.findById(id);
        if (entity.isEmpty()) {
            throw new DataValidationException(DataValidation.Status.USER_NOT_FOUND);
        }
        return mapper.toUser(entity.get());
    }
}
