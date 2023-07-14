package pot.insurance.manager.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pot.insurance.manager.dto.CompanyFunctionsDTO;
import pot.insurance.manager.service.CompanyFunctionsService;

@RestController
@RequestMapping("/v1/companies/{companyId}/functions")
@RequiredArgsConstructor
public class CompanyFunctionsRestController {

    private final CompanyFunctionsService companyFunctionsService;

    @PostMapping()
    public CompanyFunctionsDTO saveCompanyFunctions(@PathVariable UUID companyId, @RequestBody CompanyFunctionsDTO companyFunctionsDTO) {
        return companyFunctionsService.saveCompanyFunctions(companyId, companyFunctionsDTO);
    }

}
