package pot.insurance.manager.configuration;



import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
        
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                
        
                http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/v1/echo").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/**").authenticated()

                        // config for user
                        .requestMatchers(HttpMethod.GET ,"/v1/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/v1/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET ,"/v1/users/{id}").hasRole("ADMIN")
                        
                        // config for h2 console
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                );
                
                http.httpBasic(Customizer.withDefaults());
                http.csrf(csrfCustomizer ->
                        csrfCustomizer.disable());

                http.headers(headersCustomizer ->
                        headersCustomizer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
                
                return http.build();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$FzKgkghitNLmmWGWmDOD/uENyAn0fZRuRmi75uEQECFp3mmrhbN/C") // password: password
                .roles("ADMIN")
                .build();

        UserDetails viewer = User.builder()
                .username("viewer")
                .password("{bcrypt}$2a$10$FzKgkghitNLmmWGWmDOD/uENyAn0fZRuRmi75uEQECFp3mmrhbN/C") // password: password
                .roles("VIEWER")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password("{bcrypt}$2a$10$FzKgkghitNLmmWGWmDOD/uENyAn0fZRuRmi75uEQECFp3mmrhbN/C") // password: password
                .roles( "USER")
                .build();

        return new InMemoryUserDetailsManager(admin, viewer, user);
        }
}