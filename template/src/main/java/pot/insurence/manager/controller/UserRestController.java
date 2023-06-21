package pot.insurence.manager.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pot.insurence.manager.dto.UserDTO;
import pot.insurence.manager.service.UserService;

@RestController
public class UserRestController {
    
    private UserService userService;

    public UserRestController(UserService theUserService) {
        userService = theUserService;
    }

    @PostMapping("/v1/users")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        UUID uuid = UUID.randomUUID();
        userDTO.setId(uuid);
        UserDTO dbUser = userService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dbUser);
    }

    @GetMapping("/v1/users")
    public ResponseEntity<Object> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping("/v1/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID id){
        UserDTO user = userService.getUserById(id);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found! Wrong id - " + id );
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
