package pot.insurance.manager.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import pot.insurance.manager.auth.BasicUserDetails;
import pot.insurance.manager.entity.user.BasicUser;
import pot.insurance.manager.repository.BasicUserRepository;

@Service
public class BasicUserDetailsService implements UserDetailsService {

	private final BasicUserRepository repository;

	public BasicUserDetailsService(BasicUserRepository repository) {
		// TODO: Handling for null exception maybe?
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		BasicUser user = this.repository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("user with username " + username + " is not found");
		}
		return new BasicUserDetails(user);
	}

}
