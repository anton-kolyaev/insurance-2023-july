package pot.insurence.manager.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pot.insurence.manager.dao.CompanyDAO;
import pot.insurence.manager.dto.CompanyDTO;

@AllArgsConstructor
@Service
public class CompanyService {
    @Autowired
    CompanyDAO companyDAO;
    public void saveCompany(CompanyDTO companyDTO) {
        companyDAO.save(companyDTO);
    }
}
