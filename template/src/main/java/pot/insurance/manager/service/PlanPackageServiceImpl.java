package pot.insurance.manager.service;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pot.insurance.manager.dto.PlanDTO;
import pot.insurance.manager.dto.PlanPackageDTO;
import pot.insurance.manager.entity.Plan;
import pot.insurance.manager.entity.PlanPackage;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.mapper.PlanMapper;
import pot.insurance.manager.mapper.PlanPackageMapper;
import pot.insurance.manager.repository.PlanPackageRepository;
import pot.insurance.manager.repository.PlanRepository;
import pot.insurance.manager.type.DataValidation;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanPackageServiceImpl implements PlanPackageService {
    private final PlanPackageRepository packageRepository;
    private final PlanRepository planRepository;
    private final PlanPackageMapper packageMapper;
    private final PlanMapper planMapper;

    @Override
    public PlanPackageDTO save(PlanPackageDTO planPackage) {
        PlanPackageMapper mapper = this.packageMapper;
        PlanPackageRepository repository = this.packageRepository;

        PlanPackage entity = mapper.toEntity(planPackage);
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        if (repository.findById(entity.getId()).isPresent()) {
            throw new DataValidationException(DataValidation.Status.PLAN_PACKAGE_ID_EXISTS);
        }
        try {
            PlanPackageDTO finalPlanPackage = mapper.toDTO(repository.save(entity));
            Collection<PlanDTO> plans = planPackage.getPlans();
            if (plans != null) {
                finalPlanPackage.setPlans(plans
                    .stream()
                    .map(dto -> this.savePlan(finalPlanPackage, dto))
                    .toList()
                );
            } else {
                finalPlanPackage.setPlans(Collections.emptyList());
            }
            return finalPlanPackage;
        } catch (DataIntegrityViolationException exception) {
            throw new DataValidationException(DataValidation.Status.PLAN_PACKAGE_INVALID_DATA);
        }

    }

    private PlanDTO savePlan(PlanPackageDTO planPackage, PlanDTO plan) {
        PlanRepository repository = this.planRepository;
        PlanMapper mapper = this.planMapper;

        Plan entity = mapper.toEntity(plan);
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        if (entity.getPackageId() == null) {
            entity.setPackageId(planPackage.getId());
        }
        if (repository.findById(entity.getId()).isPresent()) {
            throw new DataValidationException(DataValidation.Status.PLAN_ID_EXISTS);
        }
        try {
            return mapper.toDTO(repository.save(entity));
        } catch (DataIntegrityViolationException exception) {
            throw new DataValidationException(DataValidation.Status.PLAN_INVALID_DATA);
        }
    }

    private PlanPackageDTO attachPlans(PlanPackageDTO dto) {
        dto.setPlans(
            this.planRepository.findPlansByPackageId(dto.getId())
                .stream()
                .map(this.planMapper::toDTO)
                .toList()
        );
        return dto;
    }

    @Override
    public List<PlanPackageDTO> findAll() {
        return this.packageRepository.findAll()
            .stream()
            .map(entity -> this.attachPlans(this.packageMapper.toDTO(entity)))
            .toList();
    }

    @Override
    public PlanPackageDTO findById(UUID id) {
        Optional<PlanPackage> planPackage = this.packageRepository.findById(id);
        if (planPackage.isEmpty()) {
            throw new DataValidationException(DataValidation.Status.PLAN_PACKAGE_NOT_FOUND);
        }
        return this.attachPlans(this.packageMapper.toDTO(planPackage.get()));
    }
}
