package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import pot.insurance.manager.dto.UserAuthDTO;
import pot.insurance.manager.entity.UserAuth;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAuthMapper {
	UserAuthDTO toDTO(UserAuth entity);

	UserAuth toEntity(UserAuthDTO dto);

}