package pot.insurance.manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import pot.insurance.manager.type.UserAuthRole;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/admin/**").hasRole(UserAuthRole.ADMIN.name())
                

                .requestMatchers("/v1/users").hasRole(UserAuthRole.ADMIN.name())
                .requestMatchers(HttpMethod.GET ,"/v1/users/{userId}").hasRole(UserAuthRole.ADMIN.name())

                .requestMatchers("/v1/user-packages").hasRole(UserAuthRole.ADMIN.name())
                .anyRequest().authenticated()
        )
        // (START)
        // Not very safe in production.
        // Vulnerable to cross-site-resource-forgery attacks
        // In production this should be configured in a way
        // that only client endpoints could reach the api.
        .csrf(AbstractHttpConfigurer::disable)
        // (END)
        .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}