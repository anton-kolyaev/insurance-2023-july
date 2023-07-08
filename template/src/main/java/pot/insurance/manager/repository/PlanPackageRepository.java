package pot.insurance.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pot.insurance.manager.entity.PlanPackage;

import java.util.UUID;

@Repository
public interface PlanPackageRepository extends JpaRepository<PlanPackage, UUID> {}
