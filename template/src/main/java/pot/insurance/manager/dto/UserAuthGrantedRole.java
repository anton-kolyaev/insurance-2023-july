package pot.insurance.manager.dto;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;

import pot.insurance.manager.type.UserAuthRole;

public class UserAuthGrantedRole implements GrantedAuthority {
	@Getter
	private final String authority;

	public UserAuthGrantedRole(UserAuthRole role) {
		this.authority = role.toString();
	}
}
