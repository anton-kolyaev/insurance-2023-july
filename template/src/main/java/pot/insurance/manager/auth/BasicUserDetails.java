package pot.insurance.manager.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import pot.insurance.manager.entity.user.BasicUser;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BasicUserDetails implements UserDetails {
	@Getter
	private final BasicUser user;
	private final List<BasicUserGrantedRole> roles;
	public BasicUserDetails(BasicUser user) {
		// TODO: Not null annotation or null check?
		this.user = user;
		this.roles = Collections.singletonList(new BasicUserGrantedRole(user.getRole()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
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
