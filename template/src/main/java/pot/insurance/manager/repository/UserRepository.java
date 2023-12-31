package pot.insurance.manager.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pot.insurance.manager.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{

    Optional<User> findByIdAndDeletionStatusFalse(UUID userId);

    Optional<User> findBySsn(String ssn);

    List<User> findAllByDeletionStatusFalse();
}
