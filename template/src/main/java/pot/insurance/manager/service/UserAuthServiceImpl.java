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

	private final static UserAuthRole DEFAULT_ROLE = UserAuthRole.VIEWER;
	private final static UserAuthStatus DEFAULT_STATUS = UserAuthStatus.ACTIVE;

	private final UserAuthRepository repository;
	private final UserAuthMapper mapper;

	private final PasswordEncoder encoder;

	private UserAuth prepareEntity(UserAuth entity) {
		if (entity.getRole() == null) {
			entity.setRole(UserAuthServiceImpl.DEFAULT_ROLE);
		}
		if (entity.getStatus() == null) {
			entity.setStatus(UserAuthServiceImpl.DEFAULT_STATUS);
		}
		entity.setUsername(entity.getUsername().toLowerCase());
		entity.setPassword(encoder.encode(entity.getPassword()));
		return entity;
	}

	@Override
	public UserAuthDTO save(UserAuthDTO dto) {
		if (this.repository.existsByUsername(dto.getUsername())) {
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
	public UserAuthDTO findAll(UUID id) {
		return this.getDTO(this.repository.findByIdAndStatusNot(id, UserAuthStatus.DELETED));
	}

	@Override
	public UserAuthDTO findAll(String username) {
		return this.getDTO(this.repository.findByUsernameAndStatusNot(username.toLowerCase(), UserAuthStatus.DELETED));
	}

	@Override
	public UserAuthDTO update(UserAuthDTO dto) {
		if (!this.exists(dto.getId())) {
			throw new DataValidationException(DataValidation.Status.USER_AUTH_NOT_FOUND);
		}
		if (this.repository.existsByUsername(dto.getUsername())) {
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
		if (!this.exists(id)) {
			throw new DataValidationException(DataValidation.Status.USER_NOT_FOUND);
		}
		this.repository.deleteById(id);
	}

	@Override
	public void delete(UserAuthDTO dto) {
		this.delete(dto.getId());
	}

	@Override
	public boolean exists(UUID id) {
		return this.repository.existsByIdAndStatusNot(id, UserAuthStatus.DELETED);
	}
	@Override
	public boolean exists(UserAuthDTO dto) {
		return this.exists(dto.getId());
	}
}
