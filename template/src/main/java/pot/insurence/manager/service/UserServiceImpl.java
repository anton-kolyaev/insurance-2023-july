package pot.insurence.manager.service;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pot.insurence.manager.dao.UserDAO;
import pot.insurence.manager.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {
    
    private UserDAO userDAO;

    public UserServiceImpl(UserDAO theUserDAO) {
        userDAO = theUserDAO;
    }
    
    @Transactional
    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        return userDAO.saveUser(userDTO);
    }
    
    @Override
    public List<UserDTO> getAllUsers(){
        return userDAO.getAllUsers();
    }

    @Override
    public UserDTO getUserById(UUID id){
        return userDAO.getUserById(id);
    }
}
