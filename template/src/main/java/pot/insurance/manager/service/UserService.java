package pot.insurance.manager.service;

import java.util.List;
import java.util.UUID;
import pot.insurance.manager.dto.UserDTO;

public interface UserService {
    
    UserDTO save(UserDTO userDTO);
    
    List<UserDTO> findAll();

    UserDTO findById(UUID userId);

    UserDTO update(UUID userId, UserDTO userDTO);

    UserDTO softDeleteById(UUID userId);
}
