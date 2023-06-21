package pot.insurance.manager.service;


import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pot.insurance.manager.dao.UserRepository;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;
import pot.insurance.manager.exception.exeptions.UserNotFoundException;
import pot.insurance.manager.exception.exeptions.UserWrongCredentialsInput;
import pot.insurance.manager.mapper.UserMapper;

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
        return users.stream().map(userMapper::userToUserDTO).toList();
    }

    @Override
    public UserDTO findById(UUID id){
        try {
            User user = userRepository.findById(id).get();
            return userMapper.userToUserDTO(user);
        } catch (RuntimeException e) {
            throw new UserNotFoundException("User not found by id - " + id);
        }
    }

}