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
@AllArgsConstructor
public class ClaimRestController {

    @Autowired
    private ClaimService claimService;

    @PostMapping("/v1/claims")
    public ResponseEntity<ClaimDTO> createClaim(@RequestBody ClaimDTO claimDTO) {
        claimService.saveClaim(claimDTO);
        return new ResponseEntity<>(claimDTO, HttpStatus.CREATED);
    }
}
