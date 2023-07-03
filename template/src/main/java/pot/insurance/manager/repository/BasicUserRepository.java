package pot.insurance.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pot.insurance.manager.entity.user.BasicUser;

import java.util.UUID;

@Repository
public interface BasicUserRepository extends JpaRepository<BasicUser, UUID> {
	BasicUser findByUsername(String username);

}
