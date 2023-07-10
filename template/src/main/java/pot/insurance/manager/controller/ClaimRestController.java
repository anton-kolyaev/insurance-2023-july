package pot.insurance.manager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pot.insurance.manager.dto.ClaimDTO;
import pot.insurance.manager.service.ClaimService;

@RestController
@RequestMapping("/v1/claims")
@RequiredArgsConstructor
public class ClaimRestController {

    private final ClaimService claimService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ClaimDTO saveClaim(@RequestBody ClaimDTO claimDTO) {
        return claimService.save(claimDTO);
    }

    @GetMapping()
    public Object findAllClaims() {
        return claimService.findAll();
    }
}
