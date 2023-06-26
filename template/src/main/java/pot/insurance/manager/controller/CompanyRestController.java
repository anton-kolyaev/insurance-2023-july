package pot.insurance.manager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pot.insurance.manager.dto.CompanyDTO;
import pot.insurance.manager.service.CompanyService;

@RestController
@AllArgsConstructor
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/v1/companies")
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        companyService.saveCompany(companyDTO);
        return new ResponseEntity<>(companyDTO, HttpStatus.CREATED);
    }
}
