package pot.insurance.manager.debug.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

import org.springframework.security.web.SecurityFilterChain;

import pot.insurance.manager.entity.user.BasicUserRole;

@Configuration
@ConditionalOnProperty("spring.h2.console.enabled")
@EnableWebSecurity
public class DebugSecurityConfiguration {
	@Order(1)
	@Bean
	public SecurityFilterChain debugSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(customizer ->
				customizer.requestMatchers(PathRequest.toH2Console()).hasRole(BasicUserRole.ADMIN.name())
						  .anyRequest().authenticated()
		).httpBasic(Customizer.withDefaults());
		http.csrf(customizer -> customizer.ignoringRequestMatchers(PathRequest.toH2Console()));
		http.headers(customizer -> customizer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
		return http.build();
	}


}
