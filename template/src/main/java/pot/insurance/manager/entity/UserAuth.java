package pot.insurance.manager.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import pot.insurance.manager.type.UserAuthRole;
import pot.insurance.manager.type.UserAuthStatus;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "user_auth")
public class UserAuth {

	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private UserAuthRole role;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private UserAuthStatus status;
}