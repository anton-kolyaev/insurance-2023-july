package pot.insurance.manager.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pot.insurance.manager.dto.UserDTO;
import pot.insurance.manager.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserRestController {

    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO saveUser(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @GetMapping()
    public List<UserDTO> findAllUsers(){
        return userService.findAll();
        
    }

    @GetMapping("/{userId}")
    public UserDTO findUserById(@PathVariable UUID userId){
        return userService.findById(userId);
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable UUID userId, @RequestBody UserDTO userDTO){
        return userService.update(userId, userDTO);
    }

    @DeleteMapping("/{userId}")
    public UserDTO deleteUserById(@PathVariable UUID userId){
        return userService.softDeleteById(userId);
    }

}
