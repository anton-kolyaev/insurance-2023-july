package pot.insurance.manager.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.Date;
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

    @JsonProperty(required = true)
    private boolean deletionStatus;

}

