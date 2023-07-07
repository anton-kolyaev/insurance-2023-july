package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.entity.Claim;

@Mapper
public interface ClaimMapper {

    ClaimMapper INSTANCE = Mappers.getMapper(ClaimMapper.class);

    ClaimDTO claimToClaimDTO(Claim Claim);

    Claim claimDTOToClaim(ClaimDTO ClaimDTO);
}
