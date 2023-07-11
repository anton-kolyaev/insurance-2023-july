package pot.insurance.manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
        // (START)
        // Not very safe in production.
        // Vulnerable to cross-site resource forgery attacks
        // In production this should be configured in a way
        // that only client endpoints could reach the api.
        .csrf(AbstractHttpConfigurer::disable)
        // (END)
        .httpBasic(Customizer.withDefaults());
        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}