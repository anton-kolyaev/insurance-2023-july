package pot.insurance.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pot.insurance.manager.entity.UserAuth;

import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, UUID> {
	UserAuth findByUsername(String username);
}
