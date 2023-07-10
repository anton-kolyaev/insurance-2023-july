package pot.insurance.manager.service;


import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;
import pot.insurance.manager.exception.UserNotFoundException;
import pot.insurance.manager.exception.UserWrongCredentialsInput;
import pot.insurance.manager.mapper.UserMapper;
import pot.insurance.manager.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    public UserServiceImpl(UserRepository theUserRepository) {
        userRepository = theUserRepository;
    }
    
    @Override
    public UserDTO save(UserDTO userDTO) {
        try {
            User user = userMapper.userDTOToUser(userDTO);
            user.setId(UUID.randomUUID());
            return userMapper.userToUserDTO(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new UserWrongCredentialsInput(e.getMessage());
        }
    }
    
    @Override
    public List<UserDTO> findAll(){
        List<User> users = userRepository.findAll();
        return users.stream().filter(user -> !user.isDeletionStatus()) .map(userMapper::userToUserDTO).toList();
    }

    @Override
    public UserDTO findById(UUID userId){
        User user = userRepository.findByIdAndDeletionStatusFalse(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found by id - " + userId));
        return userMapper.userToUserDTO(user);
}

    @Override
    public UserDTO update(UUID userId, UserDTO userDTO) {
        try {
            User user = userRepository.findByIdAndDeletionStatusFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by id - " + userId));
            user = userMapper.userDTOToUser(userDTO);
            user.setId(userId);
            return userMapper.userToUserDTO(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new UserWrongCredentialsInput(e.getMessage());
        }
    }

    @Override
    public UserDTO softDeleteById(UUID userId){
        User user = userRepository.findByIdAndDeletionStatusFalse(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found by id - " + userId));
        user.setDeletionStatus(true);
        return userMapper.userToUserDTO(userRepository.save(user));
        
    }

}
