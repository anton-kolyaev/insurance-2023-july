package pot.insurance.manager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(schema = "insurance_manager", name="companies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "email")
    private String email;
    @Column(name = "site")
    private String site;
    @Column(name = "deletion_status")
    private boolean deletionStatus;
}
