package pot.insurance.manager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pot.insurance.manager.entity.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByIdAndDeletionStatus(UUID id, boolean deletionStatus);

    List<Company> findAllByDeletionStatus(boolean deletionStatus);
}
