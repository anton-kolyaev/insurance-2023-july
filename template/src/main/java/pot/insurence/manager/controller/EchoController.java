package pot.insurence.manager.controller;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/echo")
public class EchoController {

    @GetMapping
    public String echo(Principal principal) {
        return String.format("Hello %s!", principal.getName());
    }
}
