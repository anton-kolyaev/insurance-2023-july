package pot.insurance.manager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pot.insurance.manager.entity.UserFunctions;

public interface UserFunctionsRepository extends JpaRepository<UserFunctions, UUID>{
    
}
