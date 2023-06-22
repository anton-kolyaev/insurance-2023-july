package pot.insurence.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pot.insurence.manager.domain.Company;
import pot.insurence.manager.repository.CompanyRepository;
import pot.insurence.manager.dto.CompanyDTO;

import java.util.UUID;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    public void saveCompany(CompanyDTO companyDTO) {
        Company company = Company.builder()
                .company_name(companyDTO.getCompany_name())
                .id(UUID.randomUUID())
                .country_code(companyDTO.getCountry_code())
                .email(companyDTO.getEmail())
                .site(companyDTO.getSite())
                .build();
        companyRepository.save(company);
    }
}
