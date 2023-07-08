package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import pot.insurance.manager.dto.PlanDTO;

import pot.insurance.manager.entity.Plan;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlanMapper {

    PlanDTO toDTO(Plan entity);
    Plan toEntity(PlanDTO dto);

}
