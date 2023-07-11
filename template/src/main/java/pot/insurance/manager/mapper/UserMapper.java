package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.entity.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
    UserDTO userToUserDTO(User User);

    User userDTOToUser(UserDTO UserDTO);
}
