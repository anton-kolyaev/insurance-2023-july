package pot.insurance.manager.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import pot.insurance.manager.dto.PlanPackageDTO;
import pot.insurance.manager.service.PlanPackageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plan-packages")
public class PlanRestController {

    private final PlanPackageService service;

    @GetMapping()
    public List<PlanPackageDTO> findAllPackages() {
        return this.service.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PlanPackageDTO saveUser(@RequestBody PlanPackageDTO planPackage) {
        return this.service.save(planPackage);
    }

}
