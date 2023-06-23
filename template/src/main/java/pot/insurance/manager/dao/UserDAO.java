package pot.insurance.manager.dao;


import java.util.List;
import java.util.UUID;

import pot.insurance.manager.dto.UserDTO;

public interface UserDAO {
    
    UserDTO saveUser(UserDTO theUser);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);

    boolean isSsnExist(String ssn);

    boolean isUsernameExist(String username);
}
