package pot.insurance.manager.controller;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    
    private final UserService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole(T(pot.insurance.manager.type.UserAuthRole).ADMIN.name())")
    public UserDTO save(@RequestBody UserDTO userDTO) {
        return service.save(userDTO);
    }

    @GetMapping()
    @PreAuthorize("hasRole(T(pot.insurance.manager.type.UserAuthRole).ADMIN.name())")
    public Object findAll(){
        return service.findAll();
        
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole(T(pot.insurance.manager.type.UserAuthRole).ADMIN.name())")
    public Object findById(@PathVariable UUID id){
        return service.findById(id);
    }


}
