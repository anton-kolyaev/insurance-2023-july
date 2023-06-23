package pot.insurance.manager.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import pot.insurance.manager.dto.UserDTO;

@Repository
public class UserDAOJpaImpl implements UserDAO{
    
    private EntityManager entityManager;

    public UserDAOJpaImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public UserDTO saveUser(UserDTO theUser) {
        try {
        UserDTO dbUser = entityManager.merge(theUser);
        return dbUser;
        }
        catch (Exception ex) {
            throw ex;
        }
    }
    
    @Override
    public List<UserDTO> getAllUsers(){
        try{
            List<UserDTO> users = entityManager.createQuery("from UserDTO", UserDTO.class).getResultList();
            return users;
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public UserDTO getUserById(UUID id){
        try {
        UserDTO user = entityManager.find(UserDTO.class, id);
        return user;
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public boolean isSsnExist(String ssn) {
        try {
            entityManager.createQuery("from UserDTO where ssn = :ssn", UserDTO.class)
                    .setParameter("ssn", ssn)
                    .getSingleResult();
            return true;
        }
        catch (NoResultException ex) {
            return false;
    }
}

    @Override
    public boolean isUsernameExist(String user_name){
        try {
            entityManager.createQuery("from UserDTO where user_name = :user_name", UserDTO.class)
                    .setParameter("user_name", user_name)
                    .getSingleResult();
            return true;
        }
        catch (NoResultException ex) {
            return false;
        }
    }

}
