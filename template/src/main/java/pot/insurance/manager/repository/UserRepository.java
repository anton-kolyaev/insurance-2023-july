package pot.insurance.manager.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pot.insurance.manager.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findBySsn(String ssn);
}
