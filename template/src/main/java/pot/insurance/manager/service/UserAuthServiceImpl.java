package pot.insurance.manager.service;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pot.insurance.manager.dto.UserAuthDTO;
import pot.insurance.manager.exception.DataValidationException;
import pot.insurance.manager.mapper.UserAuthMapper;
import pot.insurance.manager.repository.UserAuthRepository;
import pot.insurance.manager.entity.UserAuth;
import pot.insurance.manager.type.DataValidation;
import pot.insurance.manager.type.UserAuthRole;
import pot.insurance.manager.type.UserAuthStatus;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

	private final UserAuthRepository repository;
	private final UserAuthMapper mapper;

	private final PasswordEncoder encoder;

	private UserAuth prepareEntity(UserAuth entity) {
		if (entity.getRole() == null) {
			entity.setRole(UserAuthRole.getDefault());
		}
		if (entity.getStatus() == null) {
			entity.setStatus(UserAuthStatus.getDefault());
		}
		entity.setUsername(entity.getUsername().toLowerCase());
		entity.setPassword(encoder.encode(entity.getPassword()));
		return entity;
	}

	private void checkRequirements(UserAuthDTO dto) {
		String username = dto.getUsername();
		String password = dto.getPassword();

		if (username.isBlank() || username.length() > UserAuthServiceImpl.getMaxUsernameLength()) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_USERNAME_BAD_REQUIREMENTS);
		}
		if (password.isBlank() || password.length() > UserAuthServiceImpl.getMaxPasswordLength()) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_PASSWORD_BAD_REQUIREMENTS);
		}
	}

	@Override
	public UserAuthDTO save(UserAuthDTO dto) {
		this.checkRequirements(dto);
		if (this.repository.existsByUsernameIgnoreCase(dto.getUsername())) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_USERNAME_EXISTS);
		}
		UserAuth entity = this.mapper.toEntity(dto);
		if (entity.getId() == null) {
			entity.setId(UUID.randomUUID());
		}
		if (this.repository.existsById(entity.getId())) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_ID_EXISTS);
		}
		try {
			return this.mapper.toDTO(this.repository.save(this.prepareEntity(entity)));
		} catch (DataIntegrityViolationException exception) {
			throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
		}
	}

	private UserAuthDTO getDTO(Optional<UserAuth> entity) {
		return this.mapper.toDTO(entity.orElseThrow(() ->
			new DataValidationException(DataValidation.Status.USER_AUTH_NOT_FOUND))
		);
	}

	@Override
	public UserAuthDTO find(UUID id) {
		return this.getDTO(this.repository.findByIdAndStatusNot(id, UserAuthStatus.DELETED));
	}

	@Override
	public UserAuthDTO update(UserAuthDTO dto) {
		if (!this.repository.existsById(dto.getId())) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_NOT_FOUND);
		}
		Optional<UserAuth> optional = this.repository.findByUsernameIgnoreCase(dto.getUsername());
		if (optional.isPresent() && optional.get().getId().compareTo(dto.getId()) != 0) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_USERNAME_EXISTS);
		}
		this.checkRequirements(dto);
		UserAuth entity = this.mapper.toEntity(dto);
		try {
			return this.mapper.toDTO(this.repository.save(this.prepareEntity(entity)));
		} catch (DataIntegrityViolationException exception) {
			throw new DataValidationException(DataValidation.Status.MALFORMED_DATA);
		}
	}

	@Override
	public void delete(UUID id) {
		UserAuthDTO dto = this.find(id);
		dto.setStatus(UserAuthStatus.DELETED);
		this.update(dto);
	}

	@Override
	public boolean exists(UUID id) {
		return this.repository.existsByIdAndStatusNot(id, UserAuthStatus.DELETED);
	}

	public static int getMaxUsernameLength() {
		return 35;
	}

	public static int getMaxPasswordLength() {
		return 100;
	}
}
