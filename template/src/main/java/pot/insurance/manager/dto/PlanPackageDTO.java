package pot.insurance.manager.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import pot.insurance.manager.type.PayrollFrequency;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class PlanPackageDTO {
    @JsonProperty
    private UUID id;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private String name;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private PayrollFrequency payroll;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private LocalDate starts;

    @JsonSetter(nulls = Nulls.FAIL)
    @JsonProperty(required = true)
    private LocalDate expires;

    @JsonProperty
    private List<PlanDTO> plans;
}
