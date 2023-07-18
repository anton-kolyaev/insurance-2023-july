package pot.insurance.manager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pot.insurance.manager.entity.CompanyFunctions;

public interface CompanyFunctionsRepository extends JpaRepository<CompanyFunctions, UUID>{ }
