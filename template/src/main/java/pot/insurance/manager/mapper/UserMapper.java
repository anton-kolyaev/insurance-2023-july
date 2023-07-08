package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDTO toUser(User User);

    User toDTO(UserDTO UserDTO);
}
