package pot.insurance.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pot.insurance.manager.entity.Claim;

import java.util.UUID;

public interface ClaimRepository extends JpaRepository<Claim, UUID>{

}
