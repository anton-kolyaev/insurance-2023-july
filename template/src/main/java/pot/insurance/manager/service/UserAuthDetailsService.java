package pot.insurance.manager.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pot.insurance.manager.dto.UserAuthDetails;
import pot.insurance.manager.repository.UserAuthRepository;
import pot.insurance.manager.type.DataValidation;

@Service
@RequiredArgsConstructor
public class UserAuthDetailsService implements UserDetailsService {

	@Getter
	private final UserAuthRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new UserAuthDetails(this.repository
			.findByUsername(username.toLowerCase())
			.orElseThrow(() ->
				new UsernameNotFoundException(DataValidation.Status.USER_NOT_FOUND.getDescription())
			)
		);
	}

}
