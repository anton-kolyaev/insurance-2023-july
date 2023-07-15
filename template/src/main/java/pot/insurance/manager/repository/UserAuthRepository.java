package pot.insurance.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pot.insurance.manager.entity.UserAuth;
import pot.insurance.manager.type.UserAuthStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, UUID> {

	Optional<UserAuth> findByIdAndStatusNot(UUID id, UserAuthStatus status);

	Optional<UserAuth> findByUsernameIgnoreCase(String username);

	Optional<UserAuth> findByUsernameIgnoreCaseAndStatusNot(String username, UserAuthStatus status);

	List<UserAuth> findAllByStatusNot(UserAuthStatus status);

	boolean existsByIdAndStatusNot(UUID id, UserAuthStatus status);

	boolean existsByUsernameIgnoreCase(String username);

}
