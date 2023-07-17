package pot.insurance.manager.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pot.insurance.manager.dto.CompanyDTO;
import pot.insurance.manager.service.CompanyServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/companies")
public class CompanyRestController {

    private final CompanyServiceImpl companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole(T(pot.insurance.manager.type.UserAuthRole).ADMIN.name())")
    public CompanyDTO createCompany(@RequestBody CompanyDTO companyDTO) {
        return companyService.saveCompany(companyDTO);
    }

    @GetMapping
    public List<CompanyDTO> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{companyId}")
    public CompanyDTO getCompanyById(@PathVariable UUID companyId) {
        return companyService.getCompanyById(companyId);
    }

    @DeleteMapping("/{companyId}")
    public CompanyDTO deleteCompanyById(@PathVariable UUID companyId) {
        return companyService.deleteCompanyById(companyId);
    }
}
