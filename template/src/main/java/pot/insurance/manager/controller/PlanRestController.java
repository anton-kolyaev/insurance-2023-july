package pot.insurance.manager.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pot.insurance.manager.dto.PlanPackageDTO;
import pot.insurance.manager.service.PlanPackageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plan-packages")
public class PlanRestController {

    private final PlanPackageService service;

    @GetMapping
    // Kinda ugly. :/
    @PreAuthorize("hasRole(T(pot.insurance.manager.type.UserAuthRole).ADMIN.name())")
    public List<PlanPackageDTO> findAll() {
        return this.service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole(T(pot.insurance.manager.type.UserAuthRole).ADMIN.name())")
    public PlanPackageDTO save(@RequestBody PlanPackageDTO planPackage) {
        return this.service.save(planPackage);
    }

}
