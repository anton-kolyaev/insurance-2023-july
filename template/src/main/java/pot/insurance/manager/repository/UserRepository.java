package pot.insurance.manager.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pot.insurance.manager.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{

    List<User> findAllByDeletionStatus(boolean status);

    Optional<User> findByIdAndDeletionStatus(UUID id, boolean status);

    Optional<User> findBySsn(String ssn);

    boolean existsByIdAndDeletionStatus(UUID id, boolean deleted);

    boolean existsBySsn(String ssn);
}
