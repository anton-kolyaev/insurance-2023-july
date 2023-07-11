package pot.insurance.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pot.insurance.manager.entity.Company;
import pot.insurance.manager.dto.CompanyDTO;

@Mapper
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDTO companyToCompanyDTO(Company company);

    Company companyDTOToCompany(CompanyDTO companyDTO);
}
