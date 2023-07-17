package pot.insurance.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pot.insurance.manager.entity.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByIdAndDeletionStatusFalse(UUID companyId);
}
