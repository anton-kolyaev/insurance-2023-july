package pot.insurance.manager.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pot.insurance.manager.dto.UserFunctionsDTO;
import pot.insurance.manager.service.UserFunctionsService;

@RestController
@RequestMapping("/v1/companies/{companyId}/users/{userId}/functions")
@RequiredArgsConstructor
public class UserFunctionsRestController {

    private final UserFunctionsService companyFunctionsService;

    @PostMapping()
    public UserFunctionsDTO saveUserFunctions(@PathVariable UUID companyId, @PathVariable UUID userId, @RequestBody UserFunctionsDTO userFunctionsDTO) {
        return companyFunctionsService.saveUserFunctions(companyId, userId, userFunctionsDTO);
    }
    
    
}
