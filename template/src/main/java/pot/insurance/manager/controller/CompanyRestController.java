package pot.insurance.manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pot.insurance.manager.dto.CompanyDTO;
import pot.insurance.manager.service.CompanyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/companies")
public class CompanyRestController {

    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO createCompany(@RequestBody CompanyDTO companyDTO) {
        return companyService.saveCompany(companyDTO);
    }

    @GetMapping
    public Object getAllCompanies() {
        return companyService.getAllCompanies();
    }
}
