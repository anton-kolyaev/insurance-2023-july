package pot.insurance.manager.debug.configuration;

import jakarta.annotation.PostConstruct;

import jakarta.annotation.PreDestroy;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

import org.springframework.security.web.SecurityFilterChain;

import pot.insurance.manager.entity.UserAuth;
import pot.insurance.manager.repository.UserAuthRepository;
import pot.insurance.manager.type.UserAuthRole;
import pot.insurance.manager.type.UserAuthStatus;

import java.util.UUID;

@Order(1)
@Configuration
@EnableWebSecurity
@ConditionalOnProperty("debug")
@RequiredArgsConstructor
public class DebugSecurityConfiguration {

	private final UserAuthRepository repository;

	@Bean
	@Order(1)
	@ConditionalOnProperty("spring.h2.console.enabled")
	public SecurityFilterChain debugSecurityFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher(PathRequest.toH2Console())
		.authorizeHttpRequests(customizer ->
			customizer.anyRequest().hasRole(UserAuthRole.ADMIN.name())
		)
		.csrf(AbstractHttpConfigurer::disable)
		.headers(customizer ->
			customizer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
		)
		.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@PostConstruct
	private void addTestAuthUsers() {
		UserAuth admin = UserAuth.builder()
			.id(UUID.randomUUID())
			.username("admin")
			.password("$2a$10$fGHe9m9nCaWepdoFszUpz.MjGdot/LecvxbhmBz7CplmB3xXlDZby") // password: adminpassword
			.role(UserAuthRole.ADMIN)
			.status(UserAuthStatus.ACTIVE)
			.build();
		repository.save(admin);

		UserAuth moderator = UserAuth.builder()
			.id(UUID.randomUUID())
			.username("moderator")
			.password("$2a$10$adep3.38anG5LXIkrYFEFO0T7oxspRjsplB7II7NzlkAWVkf8sWZy") // password: moderatorpassword
			.role(UserAuthRole.MODERATOR)
			.status(UserAuthStatus.ACTIVE)
			.build();
		repository.save(moderator);

		UserAuth viewer = UserAuth.builder()
			.id(UUID.randomUUID())
			.username("viewer")
			.password("$2a$10$z7w5ZASKP6Ic/j4rGq6e3u19z3l/4pwpuGdN3OOnEwUU9dnbTnIaW") // password: viewerpassword
			.role(UserAuthRole.VIEWER)
			.status(UserAuthStatus.ACTIVE)
			.build();
		repository.save(viewer);
	}

	@PreDestroy
	private void removeTestAuthUsers() {
		this.repository
			.findByUsernameIgnoreCase("viewer")
			.ifPresent(this.repository::delete);

		this.repository
			.findByUsernameIgnoreCase("moderator")
			.ifPresent(this.repository::delete);

		this.repository
			.findByUsernameIgnoreCase("admin")
			.ifPresent(this.repository::delete);
	}
}
