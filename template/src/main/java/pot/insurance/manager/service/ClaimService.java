package pot.insurance.manager.service;
import pot.insurance.manager.dto.ClaimDTO;

import java.util.List;
import java.util.UUID;

public interface ClaimService {

    ClaimDTO save(ClaimDTO claimDTO);


    List<ClaimDTO> findAll();
}
