package pot.insurance.manager.dao;

import java.util.Optional;
import java.util.UUID;

import pot.insurance.manager.entity.User;

public interface UserRepositoryCustom {
    
    Optional<User> findByIdNotDeletedUser(UUID userId, boolean deletionStatus);

}
