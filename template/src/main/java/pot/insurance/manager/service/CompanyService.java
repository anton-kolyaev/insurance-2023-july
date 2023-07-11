package pot.insurance.manager.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pot.insurance.manager.entity.Company;
import pot.insurance.manager.dto.CompanyDTO;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.mapper.CompanyMapper;
import pot.insurance.manager.repository.CompanyRepository;
import java.util.UUID;
import pot.insurance.manager.type.DataValidation;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private static final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    public CompanyDTO saveCompany(CompanyDTO companyDTO) {
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        if(company.getId() == null) {
            company.setId(UUID.randomUUID());
        }

        Optional<Company> conflictEntity = companyRepository.findById(company.getId());
        if(conflictEntity.isPresent()) {
            throw new DataValidationException(DataValidation.Status.COMPANY_ID_EXISTS);
        }
        try {
            return companyMapper.companyToCompanyDTO(companyRepository.save(company));
        } catch(DataIntegrityViolationException e) {
            throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
        }
    }
}