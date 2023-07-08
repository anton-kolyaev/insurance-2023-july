package pot.insurance.manager.controller;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Object findAllUsers(){
        return userService.findAll();
        
    }

    @GetMapping("/{id}")
    public Object findUserById(@PathVariable UUID id){
        return userService.findById(id);
    }


}
