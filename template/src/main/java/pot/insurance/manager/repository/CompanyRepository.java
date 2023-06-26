package pot.insurance.manager.repository;

import org.springframework.data.repository.CrudRepository;
import pot.insurance.manager.domain.Company;

import java.util.UUID;

public interface CompanyRepository extends CrudRepository<Company, UUID> {

}
