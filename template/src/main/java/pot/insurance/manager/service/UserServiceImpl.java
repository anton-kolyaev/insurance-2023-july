package pot.insurance.manager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.mapper.UserMapper;
import pot.insurance.manager.repository.UserRepository;
import pot.insurance.manager.type.DataValidation;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);

        if(user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        Optional<User> conflictEntity = userRepository.findById(user.getId());
        if (conflictEntity.isPresent()) {
            throw new DataValidationException(DataValidation.Status.USER_ID_EXISTS);
        }
        Optional<User> ssnEntity = this.userRepository.findBySsn(user.getSsn());
        if (ssnEntity.isPresent()) {
            throw new DataValidationException(DataValidation.Status.USER_SSN_EXISTS);
        }

        try {
            return userMapper.userToUserDTO(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }
    
    @Override
    public List<UserDTO> findAll(){
        List<User> users = userRepository.findAll();
        return users.stream()
            .filter(user -> !user.isDeletionStatus())
            .map(userMapper::userToUserDTO)
            .toList();
    }

    @Override
    public UserDTO findById(UUID userId){
        User user = userRepository.findByIdAndDeletionStatusFalse(userId)
            .orElseThrow(() -> new DataValidationException(DataValidation.Status.USER_NOT_FOUND));
        return userMapper.userToUserDTO(user);
}

    @Override
    public UserDTO update(UUID userId, UserDTO userDTO) {
        try {
            User user = userRepository.findByIdAndDeletionStatusFalse(userId)
                .orElseThrow(() -> new DataValidationException(DataValidation.Status.USER_NOT_FOUND));
            user = userMapper.userDTOToUser(userDTO);
            user.setId(userId);
            return userMapper.userToUserDTO(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }

    @Override
    public UserDTO softDeleteById(UUID userId){
        User user = userRepository.findByIdAndDeletionStatusFalse(userId)
            .orElseThrow(() -> new DataValidationException(DataValidation.Status.USER_NOT_FOUND));
        user.setDeletionStatus(true);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

}
