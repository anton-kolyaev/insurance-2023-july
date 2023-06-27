package pot.insurance.manager.service;


import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pot.insurance.manager.dao.UserRepository;
import pot.insurance.manager.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository theUserRepository) {
        userRepository = theUserRepository;
    }
    
    @Transactional
    @Override
    public UserDTO save(UserDTO userDTO) {
        return userRepository.save(userDTO);
    }
    
    @Override
    public List<UserDTO> findAll(){
        return userRepository.findAll();
    }

    @Override
    public UserDTO findById(UUID id){
        
        Optional<UserDTO> userDTO = userRepository.findById(id);
        UserDTO theUser = null;
        if(userDTO.isPresent()){
            
            theUser = userDTO.get();
        }
        else{
            throw new RuntimeException("User not found for id: " + id);
        }
        return theUser;
        

    }

}
