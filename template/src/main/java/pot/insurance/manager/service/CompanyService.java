package pot.insurance.manager.service;

import pot.insurance.manager.dto.CompanyDTO;

import java.util.List;
import java.util.UUID;

public interface CompanyService {

	CompanyDTO saveCompany(CompanyDTO companyDTO);

	List<CompanyDTO> getAllCompanies();

	CompanyDTO getCompanyById(UUID companyId);

	CompanyDTO deleteCompanyById(UUID companyId);

}
