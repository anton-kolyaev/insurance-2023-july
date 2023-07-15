package pot.insurance.manager.service;

import pot.insurance.manager.dto.UserAuthDTO;

import java.util.List;
import java.util.UUID;

public interface UserAuthService {

	UserAuthDTO save(UserAuthDTO dto);

	List<UserAuthDTO> findAll();

	UserAuthDTO find(UUID id);

	UserAuthDTO find(String username);

	UserAuthDTO update(UserAuthDTO dto);

	void delete(UUID id);

	void delete(UserAuthDTO dto);

	boolean exists(UUID id);

	boolean exists(UserAuthDTO dto);

	int getMaxUsernameLength();

	int getMaxPasswordLength();

}
