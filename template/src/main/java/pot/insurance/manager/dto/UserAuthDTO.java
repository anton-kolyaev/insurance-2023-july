package pot.insurance.manager.dto;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

import pot.insurance.manager.type.UserAuthRole;
import pot.insurance.manager.type.UserAuthStatus;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class UserAuthDTO {

	@JsonProperty
	private UUID id;

	@JsonProperty(required = true)
	@JsonSetter(contentNulls = Nulls.FAIL)
	private String username;

	@JsonSetter(contentNulls = Nulls.FAIL)
	@JsonProperty(
		required = true,
		access = JsonProperty.Access.WRITE_ONLY
	)
	private String password;


	@JsonProperty
	private UserAuthRole role;


	@JsonProperty
	private UserAuthStatus status;

}
