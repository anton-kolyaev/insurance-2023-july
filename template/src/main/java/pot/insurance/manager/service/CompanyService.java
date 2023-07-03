package pot.insurance.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pot.insurance.manager.entity.Company;
import pot.insurance.manager.dto.CompanyDTO;
import pot.insurance.manager.mapper.CompanyMapper;
import pot.insurance.manager.repository.CompanyRepository;

import java.util.UUID;

@Service
public class CompanyService {
    @Autowired
    private final CompanyRepository companyRepository;
    private static final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public CompanyDTO saveCompany(CompanyDTO companyDTO) {
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        company.setId(UUID.randomUUID());
        CompanyDTO savedCompany = companyMapper.companyToCompanyDTO(companyRepository.save(company));
        return savedCompany;
    }
}
