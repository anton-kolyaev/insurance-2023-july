package pot.insurance.manager.repository;

import org.springframework.data.repository.CrudRepository;
import pot.insurance.manager.domain.Claim;

import java.util.UUID;

public interface ClaimRepository extends CrudRepository<Claim, UUID> {

}
