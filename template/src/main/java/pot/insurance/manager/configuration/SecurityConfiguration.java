package pot.insurance.manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import pot.insurance.manager.entity.user.BasicUserRole;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/admin/**").hasRole(BasicUserRole.ADMIN.name())
                .requestMatchers("/v1/echo").hasAnyRole(
                    BasicUserRole.VIEWER.name(),
                    BasicUserRole.MODERATOR.name(),
                    BasicUserRole.ADMIN.name()
                )
                .anyRequest().authenticated()
        ).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}