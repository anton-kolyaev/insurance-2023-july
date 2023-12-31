package pot.insurance.manager.dto;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
public class UserDTO {

    @JsonCreator
    public UserDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty(required = true, value = "firstName") String firstName,
        @JsonProperty(required = true, value = "lastName") String lastName,
        @JsonProperty(required = true, value = "birthday") Date birthday,
        @JsonProperty(required = true, value = "email") String email,
        @JsonProperty(required = true, value = "ssn") String ssn,
        @JsonProperty("deletionStatus") boolean deletionStatus) {

            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthday = birthday;
            this.email = email;
            this.ssn = ssn;
            this.deletionStatus = deletionStatus;
    }

    @JsonProperty
    private UUID id;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String firstName;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String lastName;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private Date birthday;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String email;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String ssn;

    @Value("${deletionStatusDefaultValue:false}")
    @JsonProperty
    private boolean deletionStatus;

}

