package pot.insurance.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private UUID id;
    private String companyName;
    private String countryCode;
    private String email;
    private String site;
}

