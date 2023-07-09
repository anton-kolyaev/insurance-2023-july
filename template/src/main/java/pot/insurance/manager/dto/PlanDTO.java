package pot.insurance.manager.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pot.insurance.manager.type.PlanType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class PlanDTO {
    @JsonProperty
    private UUID id;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String name;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private PlanType type;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private BigDecimal contributions;
}
