package pot.insurence.manager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pot.insurence.manager.dto.CompanyDTO;
import pot.insurence.manager.service.CompanyService;

@RestController
@AllArgsConstructor
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/v1/companies")
    public ResponseEntity<CompanyDTO> createComment(@RequestBody CompanyDTO companyDTO) {
        companyService.saveCompany(companyDTO);
        return new ResponseEntity<>(companyDTO, HttpStatus.CREATED);
    }
}
