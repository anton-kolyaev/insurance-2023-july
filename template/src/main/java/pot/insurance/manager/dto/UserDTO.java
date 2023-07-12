package pot.insurance.manager.dto;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class UserDTO {

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
    private LocalDate birthday;

    @JsonProperty(required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    private String email;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(
        required = true,
        access = JsonProperty.Access.WRITE_ONLY
    )
    private String ssn;

    @JsonProperty(required = true)
    private boolean deletionStatus;


    @JsonProperty
    private UserAuthDTO auth;

}

