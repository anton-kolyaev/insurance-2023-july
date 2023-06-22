package pot.insurence.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pot.insurence.manager.domain.Company;
import pot.insurence.manager.dto.CompanyDTO;

import java.util.UUID;

public interface CompanyRepository extends CrudRepository<Company, UUID> {

}
