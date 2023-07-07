package pot.insurance.manager.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.service.ClaimService;

@RestController
@RequestMapping("/v1/claims")
public class ClaimRestController {

    private final ClaimService claimService;

    public ClaimRestController (ClaimService theClaimService) {
        claimService = theClaimService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ClaimDTO saveClaim(@RequestBody ClaimDTO claimDTO) {
        return claimService.saveClaim(claimDTO);
    }

    @GetMapping()
    public Object findAllClaims() {
        return claimService.findAll();
    }
}
