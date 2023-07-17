package pot.insurance.manager.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {

    @Mapping(target = "auth", ignore = true)
    UserDTO toDTO(User User);

    @Mapping(source = "auth.id", target = "authId")
    User toEntity(UserDTO UserDTO);
}
