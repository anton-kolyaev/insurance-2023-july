package pot.insurance.manager.dto;

import com.fasterxml.jackson.annotation.*;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class UserDTO {

	@JsonCreator
	public UserDTO(
		@JsonProperty("id") UUID id,
		@JsonProperty(required = true, value = "firstName") String firstName,
		@JsonProperty(required = true, value = "lastName") String lastName,
		@JsonProperty(required = true, value = "birthday") Date birthday,
		@JsonProperty(required = true, value = "email") String email,
		@JsonProperty(required = true, value = "ssn") String ssn,
		@JsonProperty("deletionStatus") boolean deletionStatus,
		@JsonProperty("auth") UserAuthDTO auth
	) {
		this.id = id;
		this.auth = auth;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.email = email;
		this.ssn = ssn;
		this.deletionStatus = deletionStatus;
	}

	@JsonProperty
	private UUID id;

	@JsonProperty(required = true)
	@JsonSetter(nulls = Nulls.FAIL)
	private String firstName;

	@JsonProperty(required = true)
	@JsonSetter(nulls = Nulls.FAIL)
	private String lastName;

	@JsonProperty(required = true)
	@JsonSetter(nulls = Nulls.FAIL)
	private Date birthday;

	@JsonProperty(required = true)
	@JsonSetter(nulls = Nulls.FAIL)
	private String email;

	@JsonSetter(nulls = Nulls.FAIL)
	@JsonProperty(
		required = true
	)
	private String ssn;

	@JsonProperty(
		access = JsonProperty.Access.WRITE_ONLY
	)
	@Value("${deletionStatusDefaultValue:false}")
	private boolean deletionStatus;

	@JsonProperty
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private UserAuthDTO auth;

}

