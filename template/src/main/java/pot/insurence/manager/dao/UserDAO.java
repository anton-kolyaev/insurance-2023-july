package pot.insurence.manager.dao;


import java.util.List;
import java.util.UUID;


import pot.insurence.manager.dto.UserDTO;

public interface UserDAO {
    
    UserDTO saveUser(UserDTO theUser);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);
}
