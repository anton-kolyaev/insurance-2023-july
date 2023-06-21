package pot.insurence.manager.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table (
        name = "companies",
        schema = "insurance_manager_v1"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID company_uuid;
    @Column(name = "company_name", length = 100)
    private String company_name;
    @Column(name = "country_code", length = 2)
    private String country_code;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "site", length = 100)
    private String site;
}

