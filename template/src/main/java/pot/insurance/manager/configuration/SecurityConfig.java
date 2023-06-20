package pot.insurance.manager.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Attention: This is a security configuration ONLY for quick start and development purposes. It is
 * not secure and should NOT be used in production.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // This is for development purposes only! Patch for h2 console auth:
        http.csrf(csrfCustomizer ->
                csrfCustomizer.ignoringRequestMatchers(PathRequest.toH2Console())
        );
        http.headers(headersCustomizer ->
                headersCustomizer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );
        //
        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/v1/echo").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/**").authenticated()
        )
        .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails viewer =
                User.withDefaultPasswordEncoder()
                        .username("viewer")
                        .password("password")
                        .roles("VIEWER")
                        .build();

        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("password")
                        .roles("ADMIN", "USER")
                        .build();

        return new InMemoryUserDetailsManager(viewer, user, admin);
    }

}
