package pot.insurance.manager.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pot.insurance.manager.entity.User;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findByIdNotDeletedUser(UUID userId, boolean deletionStatus) {
        String query = "SELECT u FROM User u WHERE u.id = :userId AND u.deletionStatus = :deletionStatus";
        return entityManager.createQuery(query, User.class)
                .setParameter("userId", userId)
                .setParameter("deletionStatus", deletionStatus)
                .getResultList()
                .stream()
                .findFirst();
    }
}
