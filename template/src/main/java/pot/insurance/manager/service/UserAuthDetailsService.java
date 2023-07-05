package pot.insurance.manager.service;

import lombok.Getter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pot.insurance.manager.dto.UserAuthDetails;
import pot.insurance.manager.entity.UserAuth;
import pot.insurance.manager.repository.UserAuthRepository;

@Service
public class UserAuthDetailsService implements UserDetailsService {

	@Getter
	private final UserAuthRepository repository;

	public UserAuthDetailsService(UserAuthRepository repository) {
		// TODO: Handling for null exception maybe?
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAuth auth = this.repository.findByUsername(username);
		if (auth == null) {
			throw new UsernameNotFoundException("user with username " + username + " is not found");
		}
		return new UserAuthDetails(auth);
	}

}
