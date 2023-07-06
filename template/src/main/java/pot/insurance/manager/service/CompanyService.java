package pot.insurance.manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pot.insurance.manager.entity.Company;
import pot.insurance.manager.dto.CompanyDTO;
import pot.insurance.manager.exception.CompanyWrongCredentialsException;
import pot.insurance.manager.mapper.CompanyMapper;
import pot.insurance.manager.repository.CompanyRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private static final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    public CompanyDTO saveCompany(CompanyDTO companyDTO) {
        try {
            Company company = companyMapper.companyDTOToCompany(companyDTO);
            company.setId(UUID.randomUUID());
            CompanyDTO savedCompany = companyMapper.companyToCompanyDTO(companyRepository.save(company));
            return savedCompany;
        } catch (DataIntegrityViolationException e) {
            throw new CompanyWrongCredentialsException(e.getMessage());
        }
    }
}
