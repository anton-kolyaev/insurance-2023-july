package pot.insurance.manager.dao;


import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import pot.insurance.manager.dto.UserDTO;

public interface UserRepository extends JpaRepository<UserDTO, UUID>{

}
