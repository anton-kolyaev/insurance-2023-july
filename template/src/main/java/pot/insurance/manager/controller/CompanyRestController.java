package pot.insurance.manager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pot.insurance.manager.dto.CompanyDTO;
import pot.insurance.manager.service.CompanyService;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/companies")
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO response = companyService.saveCompany(companyDTO);
        return response;
    }
}
