package pot.insurance.manager.service;


import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pot.insurance.manager.repository.ClaimRepository;
import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.entity.Claim;
import pot.insurance.manager.mapper.ClaimMapper;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private static final ClaimMapper claimMapper = ClaimMapper.INSTANCE;

    public ClaimServiceImpl(ClaimRepository theClaimRepository) {
        claimRepository = theClaimRepository;
    }

    @Override
    public ClaimDTO save(ClaimDTO claimDTO) {
        try {
            Claim claim = claimMapper.claimDTOToClaim(claimDTO);
            claim.setId(UUID.randomUUID());
            return claimMapper.claimToClaimDTO(claimRepository.save(claim));
        } catch (DataIntegrityViolationException e) {
            throw new ClaimWrongCredentialsInput(e.getMessage());
        }
    }

    @Override
    public List<ClaimDTO> findAll(){
        List<Claim> claims = claimRepository.findAll();
        return claims.stream().map(claimMapper::claimToClaimDTO).toList();
    }
}
