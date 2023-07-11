package pot.insurance.manager.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class CompanyDTO {

    @JsonProperty
    private UUID id;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String companyName;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String countryCode;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String email;

    @JsonProperty
    private String site;
}

