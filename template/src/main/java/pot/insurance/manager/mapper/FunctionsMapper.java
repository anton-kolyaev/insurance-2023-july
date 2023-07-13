package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pot.insurance.manager.dto.CompanyFunctionsDTO;
import pot.insurance.manager.entity.CompanyFunctions;


@Mapper
public interface FunctionsMapper {

    FunctionsMapper INSTANCE = Mappers.getMapper(FunctionsMapper.class);
    
    CompanyFunctionsDTO companyFunctionsEntityToDTO(CompanyFunctions companyFunctions);

    CompanyFunctions companyFunctionsDTOToEntity(CompanyFunctionsDTO companyFunctionsDTO);

    
}
