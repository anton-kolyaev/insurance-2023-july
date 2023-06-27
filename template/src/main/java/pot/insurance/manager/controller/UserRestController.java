package pot.insurance.manager.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.service.UserService;

@RestController
public class UserRestController {
    
    @Autowired
    private UserService userService;

    public UserRestController(UserService theUserService) {
        userService = theUserService;
    }

    @PostMapping("/v1/users")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {

        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        UUID uuid = UUID.randomUUID();
        userDTO.setId(uuid);
        UserDTO dbUser = userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dbUser);
    }

    @GetMapping("/v1/users")
    public ResponseEntity<Object> findAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/v1/users/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable UUID id){
        UserDTO user = userService.findById(id);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
