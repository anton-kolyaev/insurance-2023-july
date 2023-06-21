package pot.insurence.manager.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import pot.insurence.manager.dto.UserDTO;

@Repository
public class UserDAOJpaImpl implements UserDAO{
    
    private EntityManager entityManager;

    public UserDAOJpaImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public UserDTO saveUser(UserDTO theUser) {
        UserDTO dbUser = entityManager.merge(theUser);
        return dbUser;
    }
    
    @Override
    public List<UserDTO> getAllUsers(){
        List<UserDTO> users = entityManager.createQuery("from UserDTO", UserDTO.class).getResultList();
        return users;
    }

    @Override
    public UserDTO getUserById(UUID id){
        UserDTO user = entityManager.find(UserDTO.class, id);
        return user;
    }
    
}
