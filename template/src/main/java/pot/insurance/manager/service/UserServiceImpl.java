package pot.insurance.manager.service;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pot.insurance.manager.dao.UserDAO;
import pot.insurance.manager.dto.UserDTO;

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

    @Override
    public boolean isSsnExist(String ssn){
        return userDAO.isSsnExist(ssn);
    }

    @Override
    public boolean isUsernameExist(String username){
        return userDAO.isUsernameExist(username);
    }
}
