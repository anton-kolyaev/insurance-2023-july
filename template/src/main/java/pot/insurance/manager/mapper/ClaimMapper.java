package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.entity.Claim;

@Mapper
public interface ClaimMapper {

    ClaimMapper INSTANCE = Mappers.getMapper(ClaimMapper.class);
    
    @Mapping(target = "claimId", source = "Claim.id")
    ClaimDTO claimToClaimDTO(Claim Claim);

    @Mapping(target = "id", source = "ClaimDTO.claimId")
    Claim claimDTOToClaim(ClaimDTO ClaimDTO);
}
