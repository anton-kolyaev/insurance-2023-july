package pot.insurance.manager.service;

import java.util.List;
import java.util.UUID;
import pot.insurance.manager.dto.UserDTO;

public interface UserService {
    
    UserDTO save(UserDTO userDTO);
    
    List<UserDTO> findAll();

    UserDTO find(UUID userId);

    UserDTO update(UserDTO userDTO);

    UserDTO delete(UUID userId);
}
