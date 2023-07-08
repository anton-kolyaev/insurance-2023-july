package pot.insurance.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import pot.insurance.manager.type.PayrollFrequency;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanPackageDTO {
    private UUID id;
    private String name;
    private PayrollFrequency payroll;
    private LocalDate starts;
    private LocalDate expires;
    private List<PlanDTO> plans;
}
