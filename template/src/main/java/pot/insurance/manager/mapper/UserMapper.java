package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(source = "authId", target = "auth.id")
    UserDTO toDTO(User User);

    @Mapping(source = "auth.id", target = "authId")
    User toEntity(UserDTO UserDTO);

}
