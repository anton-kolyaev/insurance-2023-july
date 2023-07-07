package pot.insurance.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pot.insurance.manager.domain.Claim;
import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.repository.ClaimRepository;

import java.util.UUID;

@Service
public class ClaimService {
    @Autowired
    ClaimRepository claimRepository;
    public void saveClaim(ClaimDTO claimDTO) {
        Claim claim = Claim.builder()
                .consumer_name(claimDTO.getConsumer_name())
                .id(UUID.randomUUID())
                .employer(claimDTO.getEmployer())
                .date(claimDTO.getDate())
                .plan(claimDTO.getPlan())
                .amount(claimDTO.getAmount())
                .status(claimDTO.getStatus())
                .build();
        claimRepository.save(claim);

    }
}
