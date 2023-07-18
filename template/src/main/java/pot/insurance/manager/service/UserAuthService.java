package pot.insurance.manager.service;

import pot.insurance.manager.dto.UserAuthDTO;

import java.util.UUID;

public interface UserAuthService {

	UserAuthDTO save(UserAuthDTO dto);

	UserAuthDTO find(UUID id);

	UserAuthDTO update(UserAuthDTO dto);

	void delete(UUID id);

	boolean exists(UUID id);

}