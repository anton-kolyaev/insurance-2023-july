package pot.insurance.manager.service;

import pot.insurance.manager.dto.PlanPackageDTO;

import java.util.List;
import java.util.UUID;

public interface PlanPackageService {
    PlanPackageDTO save(PlanPackageDTO planPackage);

    List<PlanPackageDTO> findAll();

    PlanPackageDTO findById(UUID id);
}
