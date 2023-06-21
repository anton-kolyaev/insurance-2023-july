package pot.insurence.manager.service;

import java.util.List;
import java.util.UUID;

import pot.insurence.manager.dto.UserDTO;

public interface UserService {
    
    UserDTO saveUser(UserDTO userDTO);
    
    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);
    
}
