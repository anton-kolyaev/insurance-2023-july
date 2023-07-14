package pot.insurance.manager.dto;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
public class CompanyDTO {

    @JsonCreator
    public CompanyDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("companyName") String companyName,
        @JsonProperty("countryCode") String countryCode,
        @JsonProperty("email") String email,
        @JsonProperty("site") String site,
        @JsonProperty("deletionStatus") Boolean deletionStatus) {

            this.id = id;
            this.companyName = companyName;
            this.countryCode = countryCode;
            this.email = email;
            this.site = site;
            this.deletionStatus = deletionStatus;
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

    @Value("${deletionStatusDefaultValue:false}")
    @JsonProperty
    private Boolean deletionStatus;
}

