package pot.insurance.manager.dao;


import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import pot.insurance.manager.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{

}
