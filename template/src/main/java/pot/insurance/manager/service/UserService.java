package pot.insurance.manager.service;

import java.util.List;
import java.util.UUID;

import pot.insurance.manager.dto.UserDTO;

public interface UserService {
    
    UserDTO saveUser(UserDTO userDTO);
    
    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);

    // is ssn exist checker
    boolean isSsnExist(String ssn);
    
    boolean isUsernameExist(String username);

}
