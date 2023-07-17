package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pot.insurance.manager.dto.CompanyFunctionsDTO;
import pot.insurance.manager.entity.CompanyFunctions;


@Mapper
public interface CompanyFunctionsMapper {

    CompanyFunctionsMapper INSTANCE = Mappers.getMapper(CompanyFunctionsMapper.class);
    
    CompanyFunctionsDTO companyFunctionsEntityToDTO(CompanyFunctions companyFunctions);

    CompanyFunctions companyFunctionsDTOToEntity(CompanyFunctionsDTO companyFunctionsDTO);

    
}
