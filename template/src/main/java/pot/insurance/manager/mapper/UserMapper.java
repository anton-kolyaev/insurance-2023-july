package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
    @Mapping(target = "userId", source = "User.id")
    UserDTO userToUserDTO(User User);

    @Mapping(target = "id", source = "UserDTO.userId")
    User userDTOToUser(UserDTO UserDTO);
}
