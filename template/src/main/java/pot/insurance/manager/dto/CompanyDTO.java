package pot.insurance.manager.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class CompanyDTO {

    @JsonCreator
    public CompanyDTO(@JsonProperty("id") UUID id, @JsonProperty(required = true, value = "companyName") String companyName, @JsonProperty(required = true, value = "countryCode") String countryCode, @JsonProperty(required = true, value = "email") String email, @JsonProperty("site") String site) {
        this.id = id;
        this.companyName = companyName;
        this.countryCode = countryCode;
        this.email = email;
        this.site = site;
    }

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

