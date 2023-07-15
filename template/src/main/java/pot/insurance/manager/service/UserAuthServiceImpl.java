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

import java.util.List;
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

		if (username.isBlank() || username.length() > this.getMaxUsernameLength()) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_USERNAME_BAD_REQUIREMENTS);
		}
		if (password.isBlank() || password.length() > this.getMaxPasswordLength()) {
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

	@Override
	public List<UserAuthDTO> findAll() {
		return this.repository.findAllByStatusNot(UserAuthStatus.DELETED)
			.stream()
			.map(this.mapper::toDTO)
			.toList();
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
	public UserAuthDTO find(String username) {
		return this.getDTO(this.repository.findByUsernameIgnoreCaseAndStatusNot(username.toLowerCase(), UserAuthStatus.DELETED));
	}

	@Override
	public UserAuthDTO update(UserAuthDTO dto) {
		this.checkRequirements(dto);
		if (!this.exists(dto.getId())) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_NOT_FOUND);
		}
		if (this.repository.existsByUsernameIgnoreCase(dto.getUsername())) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_USERNAME_EXISTS);
		}
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
	public void delete(UserAuthDTO dto) {
		this.delete(dto.getId());
		dto.setStatus(UserAuthStatus.DELETED);
	}

	@Override
	public boolean exists(UUID id) {
		return this.repository.existsByIdAndStatusNot(id, UserAuthStatus.DELETED);
	}
	@Override
	public boolean exists(UserAuthDTO dto) {
		return this.exists(dto.getId());
	}

	@Override
	public int getMaxUsernameLength() {
		return 35;
	}

	@Override
	public int getMaxPasswordLength() {
		return 100;
	}
}
