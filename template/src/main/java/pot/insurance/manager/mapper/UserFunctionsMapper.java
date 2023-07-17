package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pot.insurance.manager.dto.UserFunctionsDTO;
import pot.insurance.manager.entity.UserFunctions;

@Mapper
public interface UserFunctionsMapper {

    UserFunctionsMapper INSTANCE = Mappers.getMapper(UserFunctionsMapper.class);

    UserFunctionsDTO userFunctionsEntityToDTO(UserFunctions userFunctions);

    UserFunctions userFunctionsDTOToEntity(UserFunctionsDTO userFunctionsDTO);
    
}
