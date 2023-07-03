package pot.insurance.manager.debug.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pot.insurance.manager.entity.user.BasicUser;
import pot.insurance.manager.repository.BasicUserRepository;

import java.security.Principal;

@RestController
@ConditionalOnProperty("spring.h2.console.enabled")
@RequestMapping("/debug")
public class BasicUserDebugController {

    private final BasicUserRepository repository;
    public BasicUserDebugController(BasicUserRepository repository) {

        // TODO: Null pointer exception checking for this injection
        this.repository = repository;
    }

    @GetMapping("/user")
    public BasicUser outputUserData(Principal principal) {
        return repository.findByUsername(principal.getName());
    }
}
