package pot.insurance.manager.dto;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import pot.insurance.manager.entity.UserAuth;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserAuthDetails implements UserDetails {
	@Getter
	private final UserAuth auth;
	private final List<UserAuthGrantedRole> roles;
	public UserAuthDetails(UserAuth auth) {
		// TODO: Not null annotation or null check?
		this.auth = auth;
		this.roles = Collections.singletonList(new UserAuthGrantedRole(auth.getRole()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getPassword() {
		return this.auth.getPassword();
	}

	@Override
	public String getUsername() {
		return this.auth.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
