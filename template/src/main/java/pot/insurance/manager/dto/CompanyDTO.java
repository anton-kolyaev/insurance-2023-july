package pot.insurance.manager.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class CompanyDTO {
    private UUID id;
    private String company_name;
    private String country_code;
    private String email;
    private String site;
}

