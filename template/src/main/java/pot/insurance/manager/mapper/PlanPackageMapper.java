package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import pot.insurance.manager.dto.PlanPackageDTO;
import pot.insurance.manager.entity.PlanPackage;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlanPackageMapper {

    PlanPackageDTO toDTO(PlanPackage entity);
    PlanPackage toEntity(PlanPackageDTO dto);

}
