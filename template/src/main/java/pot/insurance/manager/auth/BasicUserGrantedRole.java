package pot.insurance.manager.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import pot.insurance.manager.entity.user.BasicUserRole;

public class BasicUserGrantedRole implements GrantedAuthority {
	@Getter
	private final String authority;

	public BasicUserGrantedRole(BasicUserRole role) {
		// TODO: CHECK FOR NULL OBJECT
		this.authority = role.toString();
	}
}
