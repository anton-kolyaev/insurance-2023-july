package pot.insurance.manager.debug.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pot.insurance.manager.entity.UserAuth;
import pot.insurance.manager.repository.UserAuthRepository;

import java.security.Principal;

@RestController
@ConditionalOnProperty("debug")
@RequestMapping("/debug/user")
public class UserAuthController {

    private final UserAuthRepository repository;
    public UserAuthController(UserAuthRepository repository) {

        // TODO: Null pointer exception checking for this injection
        this.repository = repository;
    }

    @GetMapping
    public UserAuth outputUserData(Principal principal) {
        return repository.findByUsername(principal.getName());
    }
}
