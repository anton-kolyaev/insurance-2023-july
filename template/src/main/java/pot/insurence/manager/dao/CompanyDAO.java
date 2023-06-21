package pot.insurence.manager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pot.insurence.manager.dto.CompanyDTO;

import java.util.UUID;

public interface CompanyDAO extends JpaRepository<CompanyDTO, UUID> {

}
